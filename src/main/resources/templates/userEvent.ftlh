<#import "parts/common.ftl" as c>

<@c.page>
<div class="row align-items-center">
    <div class="col-3 col-sm-3">
        <ul class="nav flex-column nav-pills">
            Events:
               <li class="nav-item">
                    <a class="nav-link" href ="/user-event/comments/${user.id}"> View new comment<#if countcomment??><br> New mess: ${countcomment}</#if></a>
                    <a class="nav-link disabled" href ="/user-event/posts/${user.id}"> View new posts in subscriptions<#if countNewPosts??> New mess: ${countNewPosts}</#if></a>
                    <a class="nav-link disabled" href ="/user-event/posts/${user.id}"> View favorite<#if countNewPosts??> New mess: ${countNewPosts}</#if></a>
               </li>
        </ul>
    </div>
    <div class="col-9">
        Choose a category to continue.
    </div>
</div>
</@c.page>