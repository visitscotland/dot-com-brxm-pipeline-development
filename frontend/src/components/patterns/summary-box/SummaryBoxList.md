## Usage

The summary box component is a styling wrapper, it has one slot which should be given a set of
4 *SummaryBoxListItem* or *SummaryBoxDistanceListItem*s

```js
    <div class="position-relative" style="height: 400px;">
        <VsSummaryBoxList>
            <VsSummaryBoxListItem
                :text=itineraries.sampleItinerary.totalDays
                label="Days"
            />
            <VsSummaryBoxDistanceListItem
                :miles=itineraries.sampleItinerary.totalMiles
                :kilometres=itineraries.sampleItinerary.totalKM
                distance-label="Distance"
                miles-label="miles"
                kilometres-label="km"
            >
            </VsSummaryBoxDistanceListItem>
            <VsSummaryBoxListItem
                :icon=itineraries.sampleItinerary.transport.key
                :iconLabel=itineraries.sampleItinerary.transport.value
                label="Transport"
            />
            <VsSummaryBoxListItem
                :icon=itineraries.sampleItinerary.theme.key
                :iconLabel=itineraries.sampleItinerary.theme.value
                label="Main theme"
            />
        </VsSummaryBoxList>
    </div>
```

They can be arranged in any combination and the presence of icons in the
*SummaryBoxListItem* elements is optional

```js
    <div class="position-relative" style="height: 400px;">
        <VsSummaryBoxList>
            <VsSummaryBoxListItem
                :text=itineraries.sampleItinerary.totalDays
                label="Days"
            />
            <VsSummaryBoxDistanceListItem
                :miles=itineraries.sampleItinerary.totalMiles
                :kilometres=itineraries.sampleItinerary.totalKM
                distance-label="Distance"
                miles-label="miles"
                kilometres-label="km"
            >
            </VsSummaryBoxDistanceListItem>
            
            <VsSummaryBoxListItem
                :text=itineraries.sampleItinerary.totalDays
                label="Transport"
            />
            <VsSummaryBoxDistanceListItem
                :miles=itineraries.sampleItinerary.totalMiles
                :kilometres=itineraries.sampleItinerary.totalKM
                distance-label="Distance"
                miles-label="miles"
                kilometres-label="km"
            >
            </VsSummaryBoxDistanceListItem>
        </VsSummaryBoxList>
    </div>
```
