<@ui.composition '/base.ftlh'>  
  <#assign title in ui>
    Restore  
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
            <h3>Restore Password</h3>
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
            <@f.form '/restore'>
              <div class="row control-group">
                <div class="form-group col-xs-12 floating-label-form-group controls">
                  <label>Email Address</label>
                  <@f.formInput 'form.email' 'form-control' 'err form-control' 'placeholder="Email Address" maxlength="50" autocomplete="off"' />
                </div>
              </div>
              <br />
              <div class="row">
                <div class="form-group col-xs-2">
                  <button type="submit" class="btn btn-success btn-lg" style="width:150%;">Submit</button>
                </div>
              </div>
            </@f.form>
          </div>
        </div>
      </div>
    </section>
  </#assign>
</@ui.composition>
