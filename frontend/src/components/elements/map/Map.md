## Usage
Use the map component to add an interactive map to the page. 

```jsx
<div style="height: 100vh">
    <VsMap
        :pins="[]"
        :labels="{}"
        mapId="vs-map"
    >
        <template slot="noJs">
            You need Javascript enabled to see this map
        </template>
    </VsMap>
</div>
```
