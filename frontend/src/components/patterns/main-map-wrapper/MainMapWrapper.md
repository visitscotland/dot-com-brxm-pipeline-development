## Usage
The main map wrapper component displays a map and a side panel, allowing the
user to filter and search for data contained on the map.

```jsx
<VsMainMapWrapper
    categoryHeading="Explore places to go"
    :filters="maps.mapFilters"
    :places-data="maps.placesData.features"
    discover-text="discover"
    map-id="vs-map"
    initialSelected="places"
    :toggleData="[
        {
            text: 'Regions',
            value: 'regions',
        },
        {
            text: 'Places',
            value: 'places',
        },
    ]"
    buttonsLabel="Select map type"
>
    <template slot="closeSidePanelText">
        <span class="sr-only">
            Close search and filter panel
        </span>
    </template>
    <template slot="openSidePanelText">
        <span class="sr-only">
            Open search and filter panel
        </span>
    </template>
    <template slot="backBtnText">
        Go back one step
    </template>
    <template slot="resetSidePanelText">
        Reset filters
    </template>
    <template slot="noJs">
        You need Javascript enabled to see this map
    </template>
</VsMainMapWrapper>
```