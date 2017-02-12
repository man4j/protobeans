<#import "/lib/composition.ftlh" as ui />
<#import "/lib/util.ftlh" as util />
<#import "/spring.ftl" as spring />
<#import "/lib/error_block.ftl" as errorBlock />

<@ui.composition "/base.ftlh">  
  <#assign title in ui>
    Index  
  </#assign>
  
  <@ui.addScript>
    <script defer="defer">
    </script>
  </@ui.addScript>
  
  <#assign content in ui>
    <@errorBlock.show />
    
    <!-- Contact Section -->
    <section id="contact">
      <div class="container">
        <div class="row">
          <div class="col-lg-12 text-center">
            <h2>Simple Form</h2>
            <hr class="star-primary">
          </div>
        </div>

        <div class="row">
          <div class="col-lg-8 col-lg-offset-2">
            <form action="/simple" method="POST" novalidate>
              <div class="row control-group">
                <div class="form-group col-xs-12 floating-label-form-group controls">
                  <label>Name</label>
                  <input type="text" class="form-control" placeholder="Name" name="name" id="name" required data-validation-required-message="Please enter your name.">
                  <p class="help-block text-danger"></p>
                </div>
              </div>
              <div class="row control-group">
                <div class="form-group col-xs-12 floating-label-form-group controls">
                  <label>Email Address</label>
                  <input type="email" class="form-control" placeholder="Email Address" name="email" id="email" required data-validation-required-message="Please enter your email address.">
                  <p class="help-block text-danger"></p>
                </div>
              </div>
              <div class="row control-group">
                <div class="form-group col-xs-12 floating-label-form-group controls">
                  <label>Phone Number</label>
                  <input type="tel" class="form-control" placeholder="Phone Number" name="phone" id="phone" required data-validation-required-message="Please enter your phone number.">
                  <p class="help-block text-danger"></p>
                </div>
              </div>
              <div class="row control-group">
                <div class="form-group col-xs-12 floating-label-form-group controls">
                  <label>Message</label>
                  <textarea rows="5" class="form-control" placeholder="Message" name="message" id="message" required data-validation-required-message="Please enter a message."></textarea>
                  <p class="help-block text-danger"></p>
                </div>
              </div>
              <br>
              <div id="success"></div>
              <div class="row">
                <div class="form-group col-xs-12">
                  <button type="submit" class="btn btn-success btn-lg">Send</button>
                </div>
              </div>
            </form>
          </div>
        </div>
      </div>
    </section>
  </#assign>  
</@ui.composition>
