package com.hk.commons.menu;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author kevin
 * @date 2019-12-27 11:31
 */
@Data
@Accessors(chain = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class Menu {

    /**
     * 菜单id
     */
    private Long id;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 菜单Icon
     */
    private String icon;

    /**
     * 菜单 链接
     */
    private String link;

    /**
     * target
     */
    private String target;

    /**
     * 子级菜单
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Menu> children;
}
