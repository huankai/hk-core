package com.hk.core.service;

import com.hk.commons.util.StringUtils;
import com.hk.core.domain.TreePersistable;

import java.util.List;

/**
 * Tree
 *
 * @param <T>
 */
public interface TreeService<T extends TreePersistable<T>> {

    /**
     * 获取子节点
     *
     * @param t
     * @return
     */
    List<TreePersistable<T>> getChildrenNodeList(T t);

    /**
     * 是否有子节点
     *
     * @param t
     * @return
     */
    default boolean hasChildrenNode(T t) {
        return getChildrenNodeList(t).size() > 0;
    }

    /**
     * 是否为Parent节点
     *
     * @param t
     * @return
     */
    default boolean isRootNode(T t) {
        T parent = t.getParent();
        return parent == null || StringUtils.equals(parent.getId(), t.getId());
    }

    /**
     * 获取父节点
     *
     * @param t
     * @return
     */
    default T getParentNode(T t) {
        T parent = t.getParent();
        return parent == null ? null :StringUtils.equals(parent.getId(),t.getId()) ? null : parent;
    }
//	
//	List<TreeNode> generateFullTree(boolean rootCheck,boolean childCheck);
//	
//	List<TreeNode> generateFullTree(boolean rootCheck,boolean childCheck,List<String> checkIds);
//	
//	List<TreeNode> generateFullTree(boolean rootCheck,boolean childCheck,Map<String,Object> param,List<String> checkIds);
//	
//	List<TreeNode> generateFullTree(boolean rootCheck,boolean childCheck,boolean isOpen,Map<String,Object> param,List<String> checkIds);
//	
//	List<TreeNode> generateAsyncTree(boolean rootCheck,boolean childCheck);
//	
//	List<TreeNode> generateAsyncTree(boolean rootCheck,boolean childCheck,List<String> checkIds);
//	
//	List<TreeNode> generateAsyncTree(boolean rootCheck,boolean childCheck,Map<String,Object> param,List<String> checkIds);
//	
//	List<TreeNode> generateAsyncTree(boolean rootCheck,boolean childCheck,boolean isOpen,Map<String,Object> param,List<String> checkIds);


}
