## Usage
The button toggle group creates a set of radio inputs that are styled as buttons.
They only allow one to be selected at a time.

It takes three props - one which gives the data for the buttons, another which identifies
the initially selected item and a third which provides a label for the buttons. This label
is hidden from users and designed to give those using screenreaders some context.

```jsx
    <VsButtonToggleGroup
        buttonsLabel="select map type"
        initialSelected="places"
        :options="[
            {
                text: 'Regions',
                value: 'regions',
                icon: 'map',
            },
            {
                text: 'Places',
                value: 'places',
                icon: 'pin',
            },
        ]"
    />
```