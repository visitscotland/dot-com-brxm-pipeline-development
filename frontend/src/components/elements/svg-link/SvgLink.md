## Usage
A standalone SVG link is commonly used for navigational purposes like linking logos to the homepage. 

### Default
Pass in the relevant SVG props for your image and add a URL. You must also include `link-alt-text` for screen readers. 
```jsx
    <VsSvgLink
        link-alt-text="VisitScotland Home"
        href="#"
        svg-fill="#700e57"
        svg-path="visitscotland"
        svg-width="300px"
    />
```
### Dark Theme
If using an SVG link on a dark background, use the `on-dark` link variant for better colour contrast on focus. 
```jsx
    <BsWrapper class="d-flex flex-wrap bg-dark p-4 mb-4">
        <VsSvgLink
            link-alt-text="VisitScotland Home"
            href="#"
            svg-fill="#700e57"
            svg-path="vs-logo"
            svg-width="300px"
            linkVariant="on-dark"
        />
    </BsWrapper>
```

## Accessibility
As an SVG link is a functional image, you must add alternative text for screen readers 
to understand the action that will be initiated rather than describing the image. 