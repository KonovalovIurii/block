<#include "security.ftl">
<div class="card-columns">
    <#list publications as publication>
    <div class="card my-3">
        <#if publication.filename??>
        <img src="/img/${publication.filename}" class="card-img-top">
        </#if>
        <div class="m-2">
            <span>${publication.text}</span> <br/>
            <i>#${publication.tag}</i>
        </div>
        <div class="card-footer text-muted">
            <#if publication.author??>
                <a class="btn btn-outline-warning" href ="/user-publications/${publication.author.id}"> ${publication.authorName} </a>

            <#if publication.author.id == currentUserId>
                <a class="btn btn-dark" href ="/user-publications/${publication.author.id}?publication=${publication.id}">
                    Edit
                </a>
            </#if>
            </#if>

             <a class="btn btn-outline-dark" href ="/publication/comment/${currentUserId}?publication=${publication.id}">
                 View comments
             </a>
             <#if publication.countNewMessage ??>
                New comments: ${publication.countNewMessage}
             </#if>

        </div>
    </div>
    <#else>
    No publication
    </#list>
</div>
