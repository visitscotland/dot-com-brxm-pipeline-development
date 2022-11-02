## Usage
The button toggle group creates a set of radio inputs that are styles as buttons.
They only allow one to be selected at a time.

It takes two props - one which gives the data for the buttons and another which identifies
the initially selected item.

```jsx
    <VsButtonToggleGroup
        selected="places"
        :options="[
            {
                text: 'Regions',
                value: 'regions',
            },
            {
                text: 'Radio 2',
                value: 'places',
            },
        ]"
    />
```