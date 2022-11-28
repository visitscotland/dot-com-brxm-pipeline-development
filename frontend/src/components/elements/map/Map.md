## Usage
Use the map component to add an interactive map to the page. 

```jsx
<div style="height: 100vh">
    <VsMap
        :places="maps.placesData.features"
        mapId="vs-map"
        :isVisible="true"
    >
        <template slot="noJs">
            You need Javascript enabled to see this map
        </template>
    </VsMap>
</div>