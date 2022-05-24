## Usage
Use an input when a user is expected to enter only a single line of text such as a name or email address.
If more than one sentence is needed, use a text area component instead.

All inputs must have a short, clear label and unique field name.

An input component can take the following HTML5 types: `text`, `password`, `number`,
`email`, `url`, `search` and `tel`.

### Default
A `fieldName` prop is required to ensure that the label and the input element are linked and accessible. Always
use a label with same `for` attribute as the `fieldName` prop.

```jsx
    <label for="email-address">Email address</label>
    <VsInput
        type="email"
        field-name="email-address"
    />
```

### Hint Text
Hint text should be used to give a user extra information about the input's purpose.
Avoid placing hint text inside the label text.

```jsx
    <label for="default">Email address</label>
    <VsInput
        type="email"
        field-name="hint-text-example"
        hint-text="We'll only use this to email you our newsletter."
    />
```

### Placeholder Text
Placeholder text can be used to provide an example of the type of data that is expected.

Never use placeholder text instead of a label as it disappears when a user inputs data.
```jsx
    <label for="placeholder-example">Email address</label>
    <VsInput
        placeholder="eg: john.smith@gmail.com"
        type="email"
        field-name="placeholder-example"
    />
```

### Optional Inputs
Inputs in a form that aren't defined as required in the validation rules should be automatically
appended with '(optional)' in their label.

```jsx
    <label for="optional-example">Last name (optional)</label>
    <VsInput field-name="optional-example"/>
```

### Invalid State
Invalid state is visible when a user exits the field or attempts to submit a form
without meeting validation rules. The input is given a red border to give a visual clue of the 
error and a clear validation message is shown.

```jsx
    <label for="invalid-example">First name</label>
    <span
        class="error d-block"
    >
        This field is required
    </span>
    <VsInput
        field-name="invalid-example"
        :invalid="true"
    />
```

### Validation
A set of generic validation messaging can be supplied to the `genericValidation` prop. This will be used
when there isn't a specific corresponding validation message provided to the `validationMessages` prop.

This is to allow a single set of default messaging to be used for a form, removing the need for repeating
the same message and potentially having to provide translations each time.

The example below shows a specific message provided for minimum length errors, but a generic one for
required. Interact with the field to trigger the validation. Validation is triggered on element blur until
the element has been interacted with, after which it is triggered on a change in value.

Validation uses the Vuelidate plugin. For a full list of validation rules see the [Vuelidate documentation](https://vuelidate.js.org/#validators).

```jsx
    <label for="validation-example">First name</label>
    <VsInput
        field-name="validation-example"
        :validation-rules="{'required': true, 'minLength': 3}"
        :generic-validation="{'required': 'This field is required'}"
        :validation-messages="{'minLength': 'First name must be at least 3 letters long'}"
    />
```

## Accessibility
- Input labels should clearly describe what data is required. Use hint text to provide further information.

- Ensure that validation messages are descriptive enough so users know how to enter a correct value.

- Inputs are styled with a clear focus state when the element is in focus.

- We use `aria-describedby` to ensure that validation messages are linked to the element they refer to.