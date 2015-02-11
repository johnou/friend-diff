<#--nav>
    <ul class="pager">
        <li><a href="#">Previous</a></li>
        <li><a href="#">Next</a></li>
    </ul>
</nav-->

<h3>Added friends <small>(${addedFriends?size?c})</small></h3>
<#if addedFriends?has_content>
<ul class="list-group">
    <#list addedFriends as friend>
        <li class="list-group-item">${friend.name?html}</li>
    </#list>
</ul>
</#if>

<h3>Removed friends <small>(${removedFriends?size?c})</small></h3>
<#if removedFriends?has_content>
<ul class="list-group">
    <#list removedFriends as friend>
        <li class="list-group-item">${friend.name?html}</li>
    </#list>
</ul>
</#if>

<h3>Current friends <small>(${friends?size?c} / ${totalFriends?c} visible)</small></h3>
<ul class="list-group">
<#list friends as friend>
    <li class="list-group-item"><#if friend.picture?has_content><img src="${friend.picture?html}" /> </#if><span>${friend.name?html}</span></li>
</#list>
</ul>