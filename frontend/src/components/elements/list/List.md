## Usage
Lists are used to group information making it easier to read by breaking it down into 
small chunks of text. Lists can be bulleted, numbered or have no style. 


### Unordered List
Use an unordered list when the order of the list items is not relevant.  These lists can be nested. 

```jsx
    <VsList>
        <li>Follow the Scottish Outdoor Access Code:
            <VsList>
                <li>Respecting others</li>
                <li>Caring for the environment</li>
                <li>Taking responsibility for your actions</li>
            </VsList>
        </li>
        <li>Leave no trace</li>
        <li>Use a stove rather than an open fire</li>
    </VsList>
```

### Ordered List
Use an ordered list when the items in the list are in a sequence, 
like when displaying a set of instructions. List items are automatically
 numbered by the browser. These lists can be nested. 

```jsx
    <VsList ordered>
        <li>Follow the Scottish Outdoor Access Code:
            <VsList ordered>
                <li>Respecting others</li>
                <li>Caring for the environment</li>
                <li>Taking responsibility for your actions</li>
            </VsList>
        </li>
        <li>Leave no trace</li>
        <li>Use a stove rather than an open fire</li>
    </VsList>
```

### Unstyled List
An unstyled list removes all the default styling if needed. 

```jsx
    <VsList unstyled>
        <li>Car</li>
        <li>Ferry</li>
        <li>Bike</li>
        <li>Plane</li>
        <li>Walk</li>
    </VsList>
```

### Unstyled Inline List
The unstyled list can be made inline which lines the items up horizontally. 
```jsx
    <VsList unstyled inline>
        <li class="mr-7">Car</li>
        <li class="mr-7">Ferry</li>
        <li class="mr-7">Bike</li>
        <li class="mr-7">Plane</li>
        <li class="mr-7">Walk</li>
    </VsList>
```

## Accessibility
Properly marked up lists are useful for screenreaders as they tell the user there is a 
list and how many items are in it. Therefore, you should use the correct list type for your content and 
never use a list just for layout or indentation purposes. 