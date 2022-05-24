## Usage
The checkbox component should be used when a user needs to select one or multiple options from a list or toggle a single option on and off.

Checkboxes should work independently from each other and each must have a label and unique field name provided.

If only one item needs to be selected from a list, use a radio button.

### Single Checkbox
A `fieldName` prop is required to ensure that the label and the checkbox element are linked and accessible.

```jsx
    <VsCheckbox
        field-name="checkbox-example"
        value="accepted"
        label="I accept the terms and conditions"
    />
```

### Checkbox Group
Use a `<fieldset>` element to contain a group of related checkboxes and include a `<legend>` to add a heading explaining the grouping. 

```jsx
    <fieldset>
        <legend>Select your mode of transport</legend>
        <VsCheckbox
            field-name="bus"
            value="bus"
            label="Bus"
        />
        <VsCheckbox
            field-name="train"
            value="train"
            label="Train"
        />
        <VsCheckbox
            field-name="car"
            value="car"
            label="Car"
        />
        <VsCheckbox
            field-name="walking"
            value="walking"
            label="Walking"
        />
    </fieldset>
```
### Hint Text
Users may need some help understanding how many options can be selected so we can add hint text to explain. Avoid adding hint text to the label itself.

```jsx
    <fieldset>
        <legend>Select mode of transport</legend>
        <VsCheckbox
            field-name="bus2"
            value="bus2"
            label="Bus"
            hint-text="Select all that apply"
        />
        <VsCheckbox
            field-name="train2"
            value="train2"
            label="Train"
        />
        <VsCheckbox
            field-name="car2"
            value="car2"
            label="Car"
        />
        <VsCheckbox
            field-name="walking2"
            value="walking2"
            label="Walking"
        />
    </fieldset>
```

### Optional Inputs
Inputs in a form that aren't defined as required in the validation rules should be automatically
appended with '(optional)' in their label.

```jsx
    <VsCheckbox
        field-name="checkbox-example-optional"
        value="example-6"
        label="Sign me up to the newsletter (optional)"
    />        
```

### Invalid State
Invalid state is visible when a user attempts to submit a form without meeting validation rules. 
The checkbox is given a red border to give a visual clue of the error and a clear validation message is shown.

```jsx
    <span
        class="error d-block"
    >
        This field is required
    </span>
    <VsCheckbox
        field-name="checkbox-example-invalid"
        value="example-5"
        label="I accept the terms and conditions"
        :invalid="true"
    />
```

## Accessibility
- Checkbox labels should clearly describe the function of the checkbox. Use hint text to provide further information.

- Ensure that validation messages are descriptive enough so users know how to enter a correct value.

- Ensures keyboard functionality using native browser keyboard navigation. Users can navigate using `tab`
and `space` to select the checkbox.

- Checkboxes have custom styles to ensure there is a clear focus style and larger select area for touch devices.

- We use `aria-describedby` to ensure that validation messages are linked to the element they refer to.