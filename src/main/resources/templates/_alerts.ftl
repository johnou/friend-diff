<#if actionErrors??>
<div class="alert alert-danger" role="alert">
    <#list actionErrors as status>
        <#if status_index == 0>Error occurred:</#if>
        <br />${status?html}
        <#if !status_has_next></#if>
    </#list>
</div>
</#if>