## Usage
The tooltip can be used with any button style by passing in any of the <a href="/#/Elements/Button">Button</a> 
props which are then automatically applied to the tooltip button. 

A `title` prop must be provided to display text or the tooltip won't show. 

### Default
The default tooltip is displayed at the top of the button. 

  ```jsx
    <VsTooltip
        title="Travel by bus"
        icon="bus"
        size="lg"
        icon-only
        variant="transparent"
    />
  ```

### Positioning
The placement of the tooltip can be changed by passing in a `position` prop.
  ```jsx
    <VsTooltip
        title="Top"
        position="top"
        class="mr-6 mb-4"
    > 
        Top 
    </VsTooltip>

    <VsTooltip
        title="Left"
        position="left"
        class="mr-6 mb-4"
    > 
        Left 
    </VsTooltip>

    <VsTooltip
        title="Right"
        position="right"
        class="mr-6 mb-4"
    > 
        Right 
    </VsTooltip>

    <VsTooltip
        title="Bottom"
        position="bottom"
        class="mr-6 mb-4"
    > 
        Bottom 
    </VsTooltip>
  ```
  
## Accessibility

Tooltips must be used with interactive and focusable elements such as buttons 
or form controls. Our default tooltip is a button which can be accessed by either 
focusing on it using a keyboard or hovering over the button with a mouse.  
