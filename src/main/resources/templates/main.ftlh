<#import "parts/common.ftl" as c>

<@c.page>
<div class="form-row">
    <div class="form-group col-md-6">
        <form method="get" action="/main" class="form-inline">
            <input type="text" name="filter" class="form-control" value="${filter?ifExists}" placeholder="Search by tag">
            <button type="submit" class="btn btn-success ml-2">Search</button>
        </form>
    </div>
</div>
<#include "parts/publicationEdit.ftl" />
<#include "parts/publicationList.ftl" />

</@c.page>