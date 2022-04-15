## Usage
All content should be wrapped in a container to maintain proper spacing. Avoid nesting containers. 

### Default
Containers are responsive with a fixed width by default. The max-width changes per breakpoint and those values can be found here <a href="/#/Guidelines/Page%20Layout">Page Layout Guidelines</a>.

Note: Enter full width view to see the difference between these containers.
```jsx
    <VsContainer class="style-cols">
        <VsRow>
            <VsCol cols="12">
                <p>This is a fixed-width container</p>
            </VsCol>
        </VsRow>
    </VsContainer>
```

### Fluid
Containers can also be fluid meaning they are 100% width of the page. They can be fluid all the time or up until a specific breakpoint. 
```jsx
    <VsContainer fluid class="style-cols">
        <VsRow>
            <VsCol>
                <p>This is a fluid container</p>
            </VsCol>
        </VsRow>
    </VsContainer>

    <VsContainer fluid="md" class="style-cols">
        <VsRow>
            <VsCol>
                <p>This is a fluid container up until 'md' breakpoint</p>
            </VsCol>
        </VsRow>
    </VsContainer>
```

