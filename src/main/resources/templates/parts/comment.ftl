<#include "security.ftl">

<div>
    <div class="row align-items-center">
        <div class="overflow-auto p-2 mb-12 mb-md-0 mr-md-12 bg-light">
            <#list messages as message>
            <div class="col-md-12">
                <h6>${message.author.username}:</h6>
                <p>${message.text}</p>
            </div>
            <#else>
                You haven’t started any conversation
            </#list>
        </div>
    </div>
    <div class="row align-items-end">
       <div class="col-md-12">
       <form method="post" enctype="multipart/form-data">
          <label for="chatmessage">Enter text:</label>
          <textarea class="form-control ${(textError??)?string('is-invalid', '')}"" id="text" rows="2"
                     name="text" placeholder="Enter your message"></textarea>
           <#if textError??>
              <div class="invalid-feedback">
                  ${textError}
              </div>
          </#if>
          <br>
          <input type="hidden" name="_csrf" value="${_csrf.token}" />
          <input type="hidden" name="id" value="<#if chat??>${publication.id}</#if>" />
          <div class="form-group">
             <button type="submit" class="btn btn-outline-success">Send</button>
          </div>
       </form>
       </div>

    </div>
</div>