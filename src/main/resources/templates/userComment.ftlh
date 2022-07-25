<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">

<@c.page>
<div class="row">
    <div class="col-3 col-sm-3">
        Publication with new comments:
        <div class="card my-3">
            <#if publication.filename??>
                <img src="/img/${publication.filename}" class="card-img-top">
            </#if>
            <div class="m-2">
                <span>${publication.text}</span> <br/>
                <i>#${publication.tag}</i>
            </div>
            <div class="card-footer text-muted">
                <a class="btn btn-outline-warning" href ="/user-publications/${publication.author.id}"> ${publication.authorName} </a>
                <#if publication.author.id == currentUserId>
                    <a class="btn btn-dark" href ="/user-publications/${publication.author.id}?publication=${publication.id}">
                         Edit
                    </a>
                </#if>

            </div>
        </div>
    </div>
    <div class="col-9">
        <#include "parts/comment.ftl">
    </div>
</div>
</@c.page>