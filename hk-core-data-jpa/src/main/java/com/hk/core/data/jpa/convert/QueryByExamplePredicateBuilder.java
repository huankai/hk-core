package com.hk.core.data.jpa.convert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.SingularAttribute;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.support.ExampleMatcherAccessor;
import org.springframework.data.util.DirectFieldAccessFallbackBeanWrapper;
import org.springframework.lang.Nullable;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/**
 * 使用 QueryByExample 查询过滤String 类型属性值为 "" 的条件查询
 *
 * @author: kevin
 * @date: 2018-07-10 17:50
 * @see org.springframework.data.jpa.convert.QueryByExamplePredicateBuilder
 */
public class QueryByExamplePredicateBuilder {

    private static final Set<Attribute.PersistentAttributeType> ASSOCIATION_TYPES;

    static {
        ASSOCIATION_TYPES = new HashSet<>(Arrays.asList(Attribute.PersistentAttributeType.MANY_TO_MANY,
                Attribute.PersistentAttributeType.MANY_TO_ONE, Attribute.PersistentAttributeType.ONE_TO_MANY, Attribute.PersistentAttributeType.ONE_TO_ONE));
    }

    /**
     * Extract the {@link Predicate} representing the {@link Example}.
     *
     * @param root    must not be {@literal null}.
     * @param cb      must not be {@literal null}.
     * @param example must not be {@literal null}.
     * @return never {@literal null}.
     */
    public static <T> Predicate getPredicate(Root<T> root, CriteriaBuilder cb, Example<T> example) {

        Assert.notNull(root, "Root must not be null!");
        Assert.notNull(cb, "CriteriaBuilder must not be null!");
        Assert.notNull(example, "Example must not be null!");

        ExampleMatcher matcher = example.getMatcher();

        List<Predicate> predicates = getPredicates("", cb, root, root.getModel(), example.getProbe(),
                example.getProbeType(), new ExampleMatcherAccessor(matcher), new QueryByExamplePredicateBuilder.PathNode("root", null, example.getProbe()));

        if (predicates.isEmpty()) {
            return cb.isTrue(cb.literal(true));
        }

        if (predicates.size() == 1) {
            return predicates.iterator().next();
        }

        Predicate[] array = predicates.toArray(new Predicate[predicates.size()]);

        return matcher.isAllMatching() ? cb.and(array) : cb.or(array);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    static List<Predicate> getPredicates(String path, CriteriaBuilder cb, Path<?> from, ManagedType<?> type, Object value,
                                         Class<?> probeType, ExampleMatcherAccessor exampleAccessor, QueryByExamplePredicateBuilder.PathNode currentNode) {

        List<Predicate> predicates = new ArrayList<>();
        DirectFieldAccessFallbackBeanWrapper beanWrapper = new DirectFieldAccessFallbackBeanWrapper(value);

        for (SingularAttribute attribute : type.getSingularAttributes()) {

            String currentPath = !StringUtils.hasText(path) ? attribute.getName() : path + "." + attribute.getName();

            if (exampleAccessor.isIgnoredPath(currentPath)) {
                continue;
            }

            ExampleMatcher.PropertyValueTransformer transformer = exampleAccessor.getValueTransformerForPath(currentPath);
            Optional<Object> optionalValue = transformer
                    .apply(Optional.ofNullable(beanWrapper.getPropertyValue(attribute.getName())));

            if (!optionalValue.isPresent()) {
                if (exampleAccessor.getNullHandler().equals(ExampleMatcher.NullHandler.INCLUDE)) {
                    predicates.add(cb.isNull(from.get(attribute)));
                }
                continue;
            }

            Object attributeValue = optionalValue.get();

            // 过滤属性类型为 String 且值为为 "" 查询条件,Spring data jpa 默认只是为 null
            if (attributeValue instanceof CharSequence && ((CharSequence) attributeValue).length() == 0) {
                continue;
            }

            if (attribute.getPersistentAttributeType().equals(Attribute.PersistentAttributeType.EMBEDDED)) {

                predicates.addAll(getPredicates(currentPath, cb, from.get(attribute.getName()),
                        (ManagedType<?>) attribute.getType(), attributeValue, probeType, exampleAccessor, currentNode));
                continue;
            }

            if (isAssociation(attribute)) {

                if (!(from instanceof From)) {
                    throw new JpaSystemException(new IllegalArgumentException(String
                            .format("Unexpected path type for %s. Found %s where From.class was expected.", currentPath, from)));
                }

                QueryByExamplePredicateBuilder.PathNode node = currentNode.add(attribute.getName(), attributeValue);
                if (node.spansCycle()) {
                    throw new InvalidDataAccessApiUsageException(
                            String.format("Path '%s' from root %s must not span a cyclic property reference!\r\n%s", currentPath,
                                    ClassUtils.getShortName(probeType), node));
                }

                predicates.addAll(getPredicates(currentPath, cb, ((From<?, ?>) from).join(attribute.getName()),
                        (ManagedType<?>) attribute.getType(), attributeValue, probeType, exampleAccessor, node));

                continue;
            }

            if (attribute.getJavaType().equals(String.class)) {

                Expression<String> expression = from.get(attribute);
                if (exampleAccessor.isIgnoreCaseForPath(currentPath)) {
                    expression = cb.lower(expression);
                    attributeValue = attributeValue.toString().toLowerCase();
                }

                switch (exampleAccessor.getStringMatcherForPath(currentPath)) {

                    case DEFAULT:
                    case EXACT:
                        predicates.add(cb.equal(expression, attributeValue));
                        break;
                    case CONTAINING:
                        predicates.add(cb.like(expression, "%" + attributeValue + "%"));
                        break;
                    case STARTING:
                        predicates.add(cb.like(expression, attributeValue + "%"));
                        break;
                    case ENDING:
                        predicates.add(cb.like(expression, "%" + attributeValue));
                        break;
                    default:
                        throw new IllegalArgumentException(
                                "Unsupported StringMatcher " + exampleAccessor.getStringMatcherForPath(currentPath));
                }
            } else {
                predicates.add(cb.equal(from.get(attribute), attributeValue));
            }
        }

        return predicates;
    }

    private static boolean isAssociation(Attribute<?, ?> attribute) {
        return ASSOCIATION_TYPES.contains(attribute.getPersistentAttributeType());
    }

    /**
     * {@link org.springframework.data.jpa.convert.QueryByExamplePredicateBuilder.PathNode} is used to dynamically grow a directed graph structure that allows to detect cycles within its
     * direct predecessor nodes by comparing parent node values using {@link System#identityHashCode(Object)}.
     *
     * @author Christoph Strobl
     */
    private static class PathNode {

        String name;
        
        @Nullable
        QueryByExamplePredicateBuilder.PathNode parent;
        
        List<QueryByExamplePredicateBuilder.PathNode> siblings = new ArrayList<>();
        
        @Nullable
        Object value;

        PathNode(String edge, @Nullable QueryByExamplePredicateBuilder.PathNode parent, @Nullable Object value) {

            this.name = edge;
            this.parent = parent;
            this.value = value;
        }

        QueryByExamplePredicateBuilder.PathNode add(String attribute, @Nullable Object value) {

            QueryByExamplePredicateBuilder.PathNode node = new QueryByExamplePredicateBuilder.PathNode(attribute, this, value);
            siblings.add(node);
            return node;
        }

        boolean spansCycle() {
            if (value == null) {
                return false;
            }
            String identityHex = ObjectUtils.getIdentityHexString(value);
            QueryByExamplePredicateBuilder.PathNode current = parent;

            while (current != null) {
                if (current.value != null && ObjectUtils.getIdentityHexString(current.value).equals(identityHex)) {
                    return true;
                }
                current = current.parent;
            }
            return false;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            if (parent != null) {
                sb.append(parent.toString());
                sb.append(" -");
                sb.append(name);
                sb.append("-> ");
            }

            sb.append("[{ ");
            sb.append(ObjectUtils.nullSafeToString(value));
            sb.append(" }]");
            return sb.toString();
        }
    }
}
