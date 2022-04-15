## Usage
Pass in the relevant SVG props for your image and add a URL. You must also include alternative text for screen readers. 

A standalone SVG link is commonly used for navigational purposes like linking logos to the homepage. 

```jsx
    <VsSvgLink
        link-alt-text="VisitScotland Home"
        href="#"
        svg-fill="#700e57"
        svg-path="visitscotland"
        svg-width="300px"
    />
```
## Accessibility
As an SVG link is a functional image, you must add alternative text for screen readers 
to understand the action that will be initiated rather than describing the image. 