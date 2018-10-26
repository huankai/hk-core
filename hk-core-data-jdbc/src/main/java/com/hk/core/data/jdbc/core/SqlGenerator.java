package com.hk.core.data.jdbc.core;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mapping.PersistentPropertyPath;
import org.springframework.data.mapping.PropertyHandler;
import org.springframework.data.relational.core.mapping.RelationalMappingContext;
import org.springframework.data.relational.core.mapping.RelationalPersistentEntity;
import org.springframework.data.relational.core.mapping.RelationalPersistentProperty;
import org.springframework.data.util.Lazy;
import org.springframework.data.util.StreamUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Spring Data JDBC sql 生成器类，
 * 在执行更新(update )操作时，主键字段(id)、 @CreateBy(createdBy) 和 @CreateDate(createdDate)标识的字段不需要update
 *
 * @author: sjq-278
 * @date: 2018-10-26 13:28
 * @see com.hk.core.data.jdbc.domain.AbstractAuditable#createdBy
 * @see com.hk.core.data.jdbc.domain.AbstractAuditable#createdDate
 * @see org.springframework.data.jdbc.core.SqlGenerator
 */
class SqlGenerator {

    private final RelationalPersistentEntity<?> entity;

    private final RelationalMappingContext context;

    /**
     * 所有字段集合
     */
    private final List<String> columnNames = new ArrayList<>();

    /**
     * 不包含主键 的字段集合
     */
    private final List<String> nonIdColumnNames = new ArrayList<>();

    /**
     * 声明需要更新的字段
     * 主键id，@CreateBy 和 @CreateDate 注解标识的字段不在更新时不需要修改
     */
    private final List<String> updateColumnNames = new ArrayList<>();

    private final Lazy<String> findOneSql = Lazy.of(this::createFindOneSelectSql);

    private final Lazy<String> findAllSql = Lazy.of(this::createFindAllSql);

    private final Lazy<String> findAllInListSql = Lazy.of(this::createFindAllInListSql);

    private final Lazy<String> existsSql = Lazy.of(this::createExistsSql);

    private final Lazy<String> countSql = Lazy.of(this::createCountSql);

    private final Lazy<String> updateSql = Lazy.of(this::createUpdateSql);

    private final Lazy<String> deleteByIdSql = Lazy.of(this::createDeleteSql);

    private final Lazy<String> deleteByListSql = Lazy.of(this::createDeleteByListSql);

    private final SqlGeneratorSource sqlGeneratorSource;

    SqlGenerator(RelationalMappingContext context, RelationalPersistentEntity<?> entity,
                 SqlGeneratorSource sqlGeneratorSource) {

        this.context = context;
        this.entity = entity;
        this.sqlGeneratorSource = sqlGeneratorSource;
        initColumnNames();
    }

    private void initColumnNames() {

        entity.doWithProperties((PropertyHandler<RelationalPersistentProperty>) p -> {
            // the referencing column of referenced entity is expected to be on the other side of the relation
            if (!p.isEntity()) {
                columnNames.add(p.getColumnName());
                if (!entity.isIdProperty(p)) {
                    nonIdColumnNames.add(p.getColumnName());
                    Field field = p.getField();
                    //过滤CreateBy 和 CreateDate注解标识的字段，不需要 update
                    if (field.getDeclaredAnnotation(CreatedBy.class) == null && field.getDeclaredAnnotation(CreatedDate.class) == null) {
                        updateColumnNames.add(p.getColumnName());
                    }
                }
            }
        });
    }

    /**
     * Returns a query for selecting all simple properties of an entitty, including those for one-to-one relationhships.
     * Results are filtered using an {@code IN}-clause on the id column.
     *
     * @return a SQL statement. Guaranteed to be not {@code null}.
     */
    String getFindAllInList() {
        return findAllInListSql.get();
    }

    /**
     * Returns a query for selecting all simple properties of an entitty, including those for one-to-one relationhships.
     *
     * @return a SQL statement. Guaranteed to be not {@code null}.
     */
    String getFindAll() {
        return findAllSql.get();
    }

    /**
     * Returns a query for selecting all simple properties of an entity, including those for one-to-one relationships.
     * Results are limited to those rows referencing some other entity using the column specified by
     * {@literal columnName}. This is used to select values for a complex property ({@link Set}, {@link Map} ...) based on
     * a referencing entity.
     *
     * @param columnName name of the column of the FK back to the referencing entity.
     * @param keyColumn  if the property is of type {@link Map} this column contains the map key.
     * @param ordered    whether the SQL statement should include an ORDER BY for the keyColumn. If this is {@code true}, the
     *                   keyColumn must not be {@code null}.
     * @return a SQL String.
     */
    String getFindAllByProperty(String columnName, @Nullable String keyColumn, boolean ordered) {

        Assert.isTrue(keyColumn != null || !ordered,
                "If the SQL statement should be ordered a keyColumn to order by must be provided.");

        String baseSelect = (keyColumn != null) //
                ? createSelectBuilder().column(cb -> cb.tableAlias(entity.getTableName()).column(keyColumn).as(keyColumn))
                .build()
                : getFindAll();

        String orderBy = ordered ? " ORDER BY " + keyColumn : "";

        return String.format("%s WHERE %s = :%s%s", baseSelect, columnName, columnName, orderBy);
    }

    String getExists() {
        return existsSql.get();
    }

    String getFindOne() {
        return findOneSql.get();
    }

    String getInsert(Set<String> additionalColumns) {
        return createInsertSql(additionalColumns);
    }

    String getUpdate() {
        return updateSql.get();
    }

    String getCount() {
        return countSql.get();
    }

    String getDeleteById() {
        return deleteByIdSql.get();
    }

    String getDeleteByList() {
        return deleteByListSql.get();
    }

    private String createFindOneSelectSql() {

        return createSelectBuilder() //
                .where(wb -> wb.tableAlias(entity.getTableName()).column(entity.getIdColumn()).eq().variable("id")) //
                .build();
    }

    private SelectBuilder createSelectBuilder() {

        SelectBuilder builder = new SelectBuilder(entity.getTableName());
        addColumnsForSimpleProperties(builder);
        addColumnsAndJoinsForOneToOneReferences(builder);

        return builder;
    }

    /**
     * Adds the columns to the provided {@link SelectBuilder} representing simplem properties, including those from
     * one-to-one relationships.
     *
     * @param builder The {@link SelectBuilder} to be modified.
     */
    private void addColumnsAndJoinsForOneToOneReferences(SelectBuilder builder) {

        for (RelationalPersistentProperty property : entity) {
            if (!property.isEntity() //
                    || Collection.class.isAssignableFrom(property.getType()) //
                    || Map.class.isAssignableFrom(property.getType()) //
                    ) {
                continue;
            }

            RelationalPersistentEntity<?> refEntity = context.getRequiredPersistentEntity(property.getActualType());
            String joinAlias = property.getName();
            builder.join(jb -> jb.leftOuter().table(refEntity.getTableName()).as(joinAlias) //
                    .where(property.getReverseColumnName()).eq().column(entity.getTableName(), entity.getIdColumn()));

            for (RelationalPersistentProperty refProperty : refEntity) {
                builder.column( //
                        cb -> cb.tableAlias(joinAlias) //
                                .column(refProperty.getColumnName()) //
                                .as(joinAlias + "_" + refProperty.getColumnName()) //
                );
            }
        }
    }

    private void addColumnsForSimpleProperties(SelectBuilder builder) {

        for (RelationalPersistentProperty property : entity) {

            if (property.isEntity()) {
                continue;
            }

            builder.column(cb -> cb //
                    .tableAlias(entity.getTableName()) //
                    .column(property.getColumnName()) //
                    .as(property.getColumnName()));
        }
    }

    private Stream<String> getColumnNameStream(String prefix) {

        return StreamUtils.createStreamFromIterator(entity.iterator()) //
                .flatMap(p -> getColumnNameStream(p, prefix));
    }

    private Stream<String> getColumnNameStream(RelationalPersistentProperty p, String prefix) {

        if (p.isEntity()) {
            return sqlGeneratorSource.getSqlGenerator(p.getType()).getColumnNameStream(prefix + p.getColumnName() + "_");
        } else {
            return Stream.of(prefix + p.getColumnName());
        }
    }

    private String createFindAllSql() {
        return createSelectBuilder().build();
    }

    private String createFindAllInListSql() {

        return createSelectBuilder() //
                .where(wb -> wb.tableAlias(entity.getTableName()).column(entity.getIdColumn()).in().variable("ids")) //
                .build();
    }

    private String createExistsSql() {
        return String.format("SELECT COUNT(%s) FROM %s WHERE %s = :id", entity.getIdColumn(), entity.getTableName(), entity.getIdColumn());
    }

    private String createCountSql() {
        return String.format("select count(%s) from %s", entity.getIdColumn(), entity.getTableName());
    }

    private String createInsertSql(Set<String> additionalColumns) {

        String insertTemplate = "INSERT INTO %s (%s) VALUES (%s)";

        LinkedHashSet<String> columnNamesForInsert = new LinkedHashSet<>(nonIdColumnNames);
        columnNamesForInsert.addAll(additionalColumns);

        String tableColumns = String.join(", ", columnNamesForInsert);

        String parameterNames = columnNamesForInsert.stream()//
                .map(n -> String.format(":%s", n))//
                .collect(Collectors.joining(", "));

        return String.format(insertTemplate, entity.getTableName(), tableColumns, parameterNames);
    }

    private String createUpdateSql() {

        String updateTemplate = "UPDATE %s SET %s WHERE %s = :%s";

        String setClause = updateColumnNames.stream()//
                .map(n -> String.format("%s = :%s", n, n))//
                .collect(Collectors.joining(", "));

        return String.format(updateTemplate, entity.getTableName(), setClause, entity.getIdColumn(), entity.getIdColumn());
    }

    private String createDeleteSql() {
        return String.format("DELETE FROM %s WHERE %s = :id", entity.getTableName(), entity.getIdColumn());
    }

    String createDeleteAllSql(@Nullable PersistentPropertyPath<RelationalPersistentProperty> path) {

        if (path == null) {
            return String.format("DELETE FROM %s", entity.getTableName());
        }

        RelationalPersistentEntity<?> entityToDelete = context
                .getRequiredPersistentEntity(path.getRequiredLeafProperty().getActualType());

        RelationalPersistentProperty property = path.getBaseProperty();

        String innerMostCondition = String.format("%s IS NOT NULL", property.getReverseColumnName());

        String condition = cascadeConditions(innerMostCondition, getSubPath(path));

        return String.format("DELETE FROM %s WHERE %s", entityToDelete.getTableName(), condition);
    }

    private String createDeleteByListSql() {
        return String.format("DELETE FROM %s WHERE %s IN (:ids)", entity.getTableName(), entity.getIdColumn());
    }

    String createDeleteByPath(PersistentPropertyPath<RelationalPersistentProperty> path) {

        RelationalPersistentEntity<?> entityToDelete = context
                .getRequiredPersistentEntity(path.getRequiredLeafProperty().getActualType());
        RelationalPersistentProperty property = path.getBaseProperty();

        String innerMostCondition = String.format("%s = :rootId", property.getReverseColumnName());

        String condition = cascadeConditions(innerMostCondition, getSubPath(path));

        return String.format("DELETE FROM %s WHERE %s", entityToDelete.getTableName(), condition);
    }

    private PersistentPropertyPath<RelationalPersistentProperty> getSubPath(
            PersistentPropertyPath<RelationalPersistentProperty> path) {

        int pathLength = path.getLength();

        PersistentPropertyPath<RelationalPersistentProperty> ancestor = path;

        for (int i = pathLength - 1; i > 0; i--) {
            ancestor = path.getParentPath();
        }

        return path.getExtensionForBaseOf(ancestor);
    }

    private String cascadeConditions(String innerCondition, PersistentPropertyPath<RelationalPersistentProperty> path) {

        if (path.getLength() == 0) {
            return innerCondition;
        }

        RelationalPersistentEntity<?> entity = context
                .getRequiredPersistentEntity(path.getBaseProperty().getOwner().getTypeInformation());
        RelationalPersistentProperty property = path.getRequiredLeafProperty();

        return String.format("%s IN (SELECT %s FROM %s WHERE %s)", //
                property.getReverseColumnName(), //
                entity.getIdColumn(), //
                entity.getTableName(), innerCondition //
        );
    }
}
