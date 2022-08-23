## Usage
The heading component uses scaled headings for each heading level to help users and search 
engines understand the content. 

There should only be one H1 heading per page and 
subheadings should follow the hierarchical scale where possible. 

Headings should be written in sentence case and be kept short and simple. 

### Default
```jsx
    <VsHeading>Scotland is calling (H1)</VsHeading>
    
    <VsHeading level="2">Scotland is calling (H2)</VsHeading>

    <VsHeading level="3">Scotland is calling (H3)</VsHeading>

    <VsHeading level="4">Scotland is calling (H4)</VsHeading>

    <VsHeading level="5">Scotland is calling (H5)</VsHeading>

    <VsHeading level="6">Scotland is calling (H6)</VsHeading>

```

### Thin
For levels 1-4, an alternative `thin` font version can be used.
```jsx
    <VsHeading thin>Scotland is calling (H1)</VsHeading>
    
    <VsHeading thin level="2">Scotland is calling (H2)</VsHeading>

    <VsHeading thin level="3">Scotland is calling (H3)</VsHeading>

    <VsHeading thin level="4">Scotland is calling (H4)</VsHeading>

```

### Subheadings
For levels 1-4, we allow a subheading to be added. 
```jsx
    <VsHeading level="1">
        The Standing Stone of Stenness (H1)
        <span slot="sub-heading">Orkney Islands</span>
    </VsHeading>
    <VsHeading level="2">
        The Standing Stone of Stenness (H2)
        <span slot="sub-heading">Orkney Islands</span>
    </VsHeading>
    <VsHeading level="3">
        The Standing Stone of Stenness (H3)
        <span slot="sub-heading">Orkney Islands</span>
    </VsHeading>
    <VsHeading level="4">
        The Standing Stone of Stenness (H4)
        <span slot="sub-heading">Orkney Islands</span>
    </VsHeading>
```

### Alternative
It may be required to use a lower case alternative font for accessibility purposes 
which can be used with the `alternative` prop.
```jsx
    <VsHeading level="1" alternative>Scotland is calling (H1)</VsHeading>
    <VsHeading level="2" alternative>Scotland is calling (H2)</VsHeading>
    <VsHeading level="3" alternative>Scotland is calling (H3)</VsHeading>
    <VsHeading level="4" alternative>Scotland is calling (H4)</VsHeading>
```

### Override Styles
In order to keep the correct heading hierarchy on the page, it may be necessary to change the style of a heading to match the design. 
The design system provides the `overrideStyleLevel` prop to achieve this and allow any heading to look like any other heading level. 
```jsx
    <VsHeading level="1" override-style-level="2">Scotland is calling</VsHeading>

    <VsHeading level="1" override-style-level="3">Scotland is calling</VsHeading>

    <VsHeading level="1" override-style-level="4">Scotland is calling</VsHeading>

    <VsHeading level="1" override-style-level="5">Scotland is calling</VsHeading>

    <VsHeading level="1" override-style-level="6">Scotland is calling</VsHeading>

```
