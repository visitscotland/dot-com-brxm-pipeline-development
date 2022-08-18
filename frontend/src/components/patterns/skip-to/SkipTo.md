## Usage
The skip to component allows users who navigate via keyboard to quickly focus on different areas of the page. 

```jsx
    <p>Tab into the element below to see the Skip To component appear:</p>
    <div class="border" style="overflow: hidden; position: relative;">
        <VsSkipTo>
            <template slot="skipToText">
                Skip to
            </template>
            <template slot="mainMenuText">
                Main menu
            </template>
            <template slot="mainContentText">
                Content
            </template>
            <template slot="searchText">
                Search
            </template>
            <template slot="footerText">
                Footer
            </template>
        </VsSkipTo>
    </div>
  ```

