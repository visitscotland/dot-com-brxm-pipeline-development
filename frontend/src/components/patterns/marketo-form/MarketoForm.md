## Usage
The Marketo Form component is used to submit user inputted data to a Marketo instance.

The form is created programatically from a group of JSON data files which provide data for
input types, labels, validation rules, validation messaging, recaptcha, submit button and details of
where the form should be submitted to.

**Please note the forms on this page are for demo purposes only and will not submit to Marketo.**


### Default
For the form to work, there are several required props that must be included more information on this can be found in our confluence pages. 

```jsx
<VsMarketoForm
   dataUrl="https://static.visitscotland.com/forms/newsletter-sign-up/newsletter-sign-up.json"
    messagingUrl="https://static.visitscotland.com/forms/common/messaging.json"
    countryListUrl="https://static.visitscotland.com/forms/common/countries.json"
    recaptchaKey="6LfqqfcZAAAAACbkbPaHRZTIFpKZGAPZBDkwBKhe"
    marketo-instance="marketo-instance-code"
    munchkin-id="munchkin-id-code"
    language="it"
    :is-prod="false"
>
    <template slot="submitError">
        We're sorry there's been a problem, please try again later. You can also ask to subscribe by sending an email to <vs-link href='mailto:info@visitscotland.com'>info@visitscotland.com</vs-link>.
    </template>

    <template slot="submitting">
        We're just submitting your form
    </template>
</VsMarketoForm>
```

### Status Messages
Status messages should be added to the relative slots. The Marketo form component will display these messages automatically if the form is submitting, has been submitted or if there was an error with the submission.

These messages should clearly inform the user to the status of the form and, if something has gone wrong, offer alternatives or assistance.


### Language
The Marketo Form is designed to support multiple languages. By using the `language` prop
the form can request translated content from the form data JSON and the messaging data JSON.

Note that due to the Recaptcha already being used in English on this page, a translated version isn't shown below. This will be automatically translated into the language when used in isolation.

```jsx
<VsMarketoForm
    dataUrl="https://static.visitscotland.com/forms/newsletter-sign-up/newsletter-sign-up.json"
    countryListUrl="https://static.visitscotland.com/forms/common/countries.json"
    messagingUrl="https://static.visitscotland.com/forms/common/messaging.json"
    recaptchaKey="6LfqqfcZAAAAACbkbPaHRZTIFpKZGAPZBDkwBKhe"
    marketo-instance="marketo-instance-code"
    munchkin-id="munchkin-id-code"
    language="de"
    :is-prod="false"
>
    <template slot="submitError">
        We're sorry there's been a problem, please try again later. You can also ask to subscribe by sending an email to <vs-link href='mailto:info@visitscotland.com'>info@visitscotland.com</vs-link>.
    </template>

    <template slot="submitting">
        We're just submitting your form
    </template>
</VsMarketoForm>
```

### Optional Inputs
It's good accessibility practice to highlight that a form element is an optional field rather than required. The
Marketo Form component automatically appends '(optional)' to an element if the validation rules supplied don't
define it as a required element.

In the example below, the 'Last name' field is not defined as required.

```jsx
<VsMarketoForm
    dataUrl="../../../fixtures/marketo-forms/optional-field.json"
    messagingUrl="https://static.visitscotland.com/forms/common/messaging.json"
    recaptchaKey="6LfqqfcZAAAAACbkbPaHRZTIFpKZGAPZBDkwBKhe"
    marketo-instance="marketo-instance-code"
    munchkin-id="munchkin-id-code"
    language="en"
    :is-prod="false"
>
    <template slot="submitError">
        We're sorry there's been a problem, please try again later. You can also ask to subscribe by sending an email to <vs-link href='mailto:info@visitscotland.com'>info@visitscotland.com</vs-link>.
    </template>

    <template slot="submitting">
        We're just submitting your form
    </template>
</VsMarketoForm>
```

### Conditional Inputs
An input can be given a value of 'conditional' in the data supplied, along with the conditions it needs to meet to appear on the form.
This means it will only appear once another element's value meets a certain criteria.

More than one value can be used to show the element under a range of values.

In the example below the conditional field will only shown if 'Yes' is chosen in the initial select element.

```jsx
<VsMarketoForm
    dataUrl="../../../fixtures/marketo-forms/conditional-field.json"
    messagingUrl="https://static.visitscotland.com/forms/common/messaging.json"
    recaptchaKey="6LfqqfcZAAAAACbkbPaHRZTIFpKZGAPZBDkwBKhe"
    marketo-instance="marketo-instance-code"
    munchkin-id="munchkin-id-code"
    language="en"
    :is-prod="false"
>
    <template slot="submitError">
        We're sorry there's been a problem, please try again later. You can also ask to subscribe by sending an email to <vs-link href='mailto:info@visitscotland.com'>info@visitscotland.com</vs-link>.
    </template>

    <template slot="submitting">
        We're just submitting your form
    </template>
</VsMarketoForm>
```

### No-JS Message
As the form requires Javascript to pull in data and to submit, it will not function if Javascript is disabled.

Specific instructions may be needed to allow people to complete the form's intended 
functionality another way so a no-javascript message should be defined in the form's JSON data. 

```jsx
<div class="no-js">
    <VsMarketoForm
        dataUrl="https://static.visitscotland.com/forms/newsletter-sign-up/newsletter-sign-up.json"
        messagingUrl="https://static.visitscotland.com/forms/common/messaging.json"
        recaptchaKey="6LfqqfcZAAAAACbkbPaHRZTIFpKZGAPZBDkwBKhe"
        marketo-instance="marketo-instance-code"
        munchkin-id="munchkin-id-code"
        language="en"
        :is-prod="false"
    >
        <template slot="no-js">
            <p>You need JavaScript enabled to be able to display our newsletter subscription form. You can enable this in your browser settings.</p>
        </template></p>
        </template>

        <template slot="submitError">
            We're sorry there's been a problem, please try again later.
        </template>

        <template slot="submitting">
            We're just submitting your form
        </template>
    </VsMarketoForm>
</div>
```

## Accessibility
- Form elements are programmatically created, ensuring that accessibility rules are adhered to.

- Provide clear and informative status messages. If something has gone wrong, give a user an alternative to completing the form.

- Provide a no-javascript message that allows the same outcome to be achieved using an alternative method.