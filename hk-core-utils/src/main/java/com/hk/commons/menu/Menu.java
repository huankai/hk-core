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

    private Long id;

    private String menuName;

    private String icon;

    private String link;

    private String target;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Menu> children;
}
