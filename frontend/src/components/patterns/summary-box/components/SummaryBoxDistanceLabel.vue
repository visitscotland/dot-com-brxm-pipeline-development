<template>
    <div class="vs-summary-box-distance-label text-center d-block position-absolute w-100">
        <strong class="d-block">{{ distanceLabel }}</strong>
        <div class="d-flex justify-content-center align-items-center">
            <VsButton
                @click.native="handleClick(true)"
                :class="showMiles ? 'active' : ''"
                :aria-expanded="showMiles ? 'true' : 'false'"
                variant="transparent"
                aria-controls="display_miles"
            >
                <abbr :title="milesLabel">{{ milesAbbr }}</abbr>
            </VsButton>
            <span class="separator">/</span>
            <VsButton
                @click.native="handleClick(false)"
                :class="showMiles ? '' : 'active'"
                :aria-expanded="showMiles ? 'false' : 'true'"
                variant="transparent"
                aria-controls="display_kilometres"
            >
                <abbr :title="kilometresLabel">{{ kilometresAbbr }}</abbr>
            </VsButton>
        </div>
    </div>
</template>

<script>
import summaryBoxStore from '@components/patterns/summary-box/summaryBox.store';
import VsButton from '@components/elements/button/Button';
/**
 * Summary Box Distance Label component includes toggles to change
 * the distance type displayed within the VsSummaryBoxDistance component
 *
 * @displayName Summary Box Distance Label
 */
export default {
    name: 'VsSummaryBoxDistanceLabel',
    status: 'prototype',
    release: '0.0.1',
    components: {
        VsButton,
    },
    props: {
        distanceLabel: {
            type: String,
            default: 'Distance',
        },
        milesLabel: {
            type: String,
            default: 'miles',
        },
        milesAbbr: {
            type: String,
            default: 'mi',
        },
        kilometresLabel: {
            type: String,
            default: 'kilometers',
        },
        kilometresAbbr: {
            type: String,
            default: 'km',
        },
    },
    data() {
        return {
            showMiles: true,
        };
    },
    computed: {
        isShowingMiles() {
            return summaryBoxStore.getters['summaryBox/getShowMiles'];
        },
    },
    watch: {
        isShowingMiles() {
            this.toggleShowMiles();
        },
    },
    summaryBoxStore,
    methods: {
        handleClick(value) {
            return summaryBoxStore.dispatch('summaryBox/setShowMiles', value);
        },
        toggleShowMiles() {
            this.showMiles = this.isShowingMiles;
        },
    },
};
</script>
<style lang="scss">
.vs-summary-box-distance-label {
    bottom: 5px;
    left: 0;
    line-height: 1rem;
    @include media-breakpoint-up(lg) {
        line-height: 1.125rem;
    }

    strong,
    .btn {
        font-size: 0.875rem;
        @include media-breakpoint-up(lg) {
            font-size: 1.125rem;
        }
    }
    .btn {
        padding: 0 2px;
        text-transform: initial !important;
        letter-spacing: initial !important;
        text-decoration: underline;

        &.active {
            font-weight: $font-weight-bold;
            text-decoration: underline;
        }
    }
}
</style>

<docs>

  ```jsx
    <div class="position-relative p-5">
        <VsSummaryBoxDistanceLabel
            distance-label="Distance"
            miles-label="miles"
            miles-abbr="mi"
            kilometres-label="kilometres"
            kilometres-abbr="km"
        ></VsSummaryBoxDistanceLabel>
    </div>
  ```
</docs>
