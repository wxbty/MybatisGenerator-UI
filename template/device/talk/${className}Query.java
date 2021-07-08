<#include "/macro.include"/>
<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
package ${basepackage}.${subpackage}.device.talk;

import ${basepackage}.${subpackage}.api.domain.Page;

/**
 * @author fgh
 */
public class ${className}Query extends Page {

<#list table.columns as column>
// ${column.columnAlias}
private ${column.simpleJavaType} ${column.columnNameLower};
</#list>

<#list table.columns as column>
public void set${column.columnName}(${column.simpleJavaType} value) {
		this.${column.columnNameLower} = value;
		}

public ${column.simpleJavaType} get${column.columnName}() {
		return this.${column.columnNameLower};
		}

</#list>
}

<#macro generateJavaColumns>
<#list table.columns as column>
<@generateBycondition column.sqlName>
public void set${column.columnName}(${column.simpleJavaType} value) {
		this.${column.columnNameLower} = value;
		}

public ${column.simpleJavaType} get${column.columnName}() {
		return this.${column.columnNameLower};
		}

</@generateBycondition>
</#list>
</#macro>
