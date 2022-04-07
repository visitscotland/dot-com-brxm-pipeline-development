## Usage
The VisitScotland Design System uses some Font Awesome alongside 
custom made icons. For a full list of icons and their name, see 
<a href="/#/Guidelines/Iconography">Iconography</a>. Choose icons that help enforce 
meaning and ensure they are consistent across the product. 

### Size
The default icon size is `md` (24px) which can be changed by setting one of these sizes.
```jsx
    <div class="vs-icon-preview">
        <div class="vs-icon-preview__column">
            <h4>xxs</h4>
            <p>(12px)</p>

            <VsIcon name="user" size="xxs" />
        </div>

        <div class="vs-icon-preview__column">
            <h4>xs</h4>
            <p>(16px)</p>

            <VsIcon name="user" size="xs" />
        </div>

        <div class="vs-icon-preview__column">
            <h4>sm</h4>
            <p>(20px)</p>

            <VsIcon name="user" size="sm" />
        </div>

        <div class="vs-icon-preview__column">
            <h4>md</h4>
            <p>(24px)</p>

            <VsIcon name="user" size="md" />
        </div>

        <div class="vs-icon-preview__column">
            <h4>lg</h4>
            <p>(30px)</p>

            <VsIcon name="user" size="lg" />
        </div>

        <div class="vs-icon-preview__column">
            <h4>xl</h4>
            <p>(40px)</p>

            <VsIcon name="user" size="xl" />
        </div>
    </div>
```

### Colour
All icons in the design system are black by default. Colour can be changed by adding 
one of these most common colour theme variants.

```jsx
    <BsWrapper class="p-4 d-inline">
        <VsIcon name="user" variant="primary"/>
    </BsWrapper>
    <BsWrapper class="p-4 d-inline">
        <VsIcon name="user" variant="secondary"/>
    </BsWrapper>
    <BsWrapper class="p-4 d-inline">
        <VsIcon name="user" variant="dark"/>
    </BsWrapper>
    <BsWrapper class="p-4 d-inline">
        <VsIcon name="user" variant="secondary-teal"/>
    </BsWrapper>
    <BsWrapper class="p-4 d-inline">
        <VsIcon name="user" variant="light"/>
    </BsWrapper>    
    <BsWrapper class="bg-light p-4 d-inline">
        <VsIcon name="user" variant="color-white"/>
    </BsWrapper>
```
The `customColour` prop can be used to set a specific colour when needed
 which will override the given variant.

```jsx
    <BsWrapper class="p-4 d-inline">
       <VsIcon name="user" custom-colour="#AE2940" />
    </BsWrapper>
    <BsWrapper class="p-4 d-inline">
        <VsIcon name="user" custom-colour="green" />
    </BsWrapper>
```

### Orientation
The orientation can be specified when needed. This is most useful when 
setting the desired direction of an arrow icon. 

```jsx
    <BsWrapper class="p-4 d-inline">
       <VsIcon name="chevron" orientation="up" />
    </BsWrapper>
    <BsWrapper class="p-4 d-inline">
        <VsIcon name="chevron" orientation="down" />
    </BsWrapper>
    <BsWrapper class="p-4 d-inline">
        <VsIcon name="chevron" orientation="left" />
    </BsWrapper>
    <BsWrapper class="p-4 d-inline">
        <VsIcon name="chevron" orientation="right" />
    </BsWrapper>
```
