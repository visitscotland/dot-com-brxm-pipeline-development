## Usage
To display an SVG within your product, set the `path` to the 
name of file and the SVG will fill the container it's constrained in. 

 ```jsx
    <VsSvg path="visitscotland" />
  ```

The `width` or `height` can also be set if needed. 

```jsx  
    <VsSvg path="visitscotland" height="50" class="mb-4"/>

    <hr />

    <VsSvg path="visitscotland" width="140" />
```

Use the `fill` prop to change the colour of the SVG. 
```jsx
    <VsSvg path="visitscotland" fill="#AF006E" />
```