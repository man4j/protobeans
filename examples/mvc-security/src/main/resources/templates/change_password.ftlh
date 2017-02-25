<@ui.composition '/base.ftlh'>  
  <#assign title in ui>
    Change Password
  </#assign>
  
  <@ui.addScript>
    <script defer="defer">
    </script>
  </@ui.addScript>
  
  <#assign content in ui>
    <section id="contact">
      <div class="container">
        <div class="row">
          <div class="col-lg-12 text-center">
            <h3>Change Password</h3>
            <hr class="star-primary">
          </div>
        </div>
        
        <div class="row">
          <div class="col-lg-8 col-lg-offset-2">
            <@f.showErrorsBlock />
          </div>
        </div>
        
        <div class="row">
          <div class="col-lg-8 col-lg-offset-2">
            <form action="<@f.url '/change_password' />" method="POST">
              <div class="row control-group">
                <div class="form-group col-xs-12 floating-label-form-group controls">
                  <label>Current Password</label>
                  <@f.formPasswordInput 'form.oldPassword' 'form-control' 'err form-control' 'placeholder="Current Password" maxlength="50" autocomplete="off"' />
                </div>
              </div>
              <div class="row control-group">
                <div class="form-group col-xs-12 floating-label-form-group controls">
                  <label>New Password</label>
                  <@f.formPasswordInput 'form.password' 'form-control' 'err form-control' 'placeholder="New Password" maxlength="50" autocomplete="off"' />
                </div>
              </div>
              <div class="row control-group">
                <div class="form-group col-xs-12 floating-label-form-group controls">
                  <label>Confirm password</label>
                  <@f.formPasswordInput 'form.confirmPassword' 'form-control' 'err form-control' 'placeholder="Confirm password" maxlength="50" autocomplete="off"' />
                </div>
              </div>
              <br />
              <div class="row">
                <div class="form-group col-xs-2">
                  <button type="submit" class="btn btn-success btn-lg" style="width:150%;">Submit</button>
                </div>
              </div>
            </form>
          </div>
        </div>
      </div>
    </section>
  </#assign>
</@ui.composition>
