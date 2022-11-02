<template>
    <BFormGroup
        :label="buttonsLabel"
        v-slot="{ ariaDescribedby }"
        class="vs-button-toggle-group"
        data-test="vs-button-toggle-group"
    >
        <BFormRadioGroup
            class="vs-button-toggle-group--radios"
            id="btn-radios-1"
            v-model="selected"
            :options="options"
            :aria-describedby="ariaDescribedby"
            name="radios-btn-default"
            buttons
            @change="toggleChange"
        />
    </BFormGroup>
</template>

<script>
import { BFormGroup, BFormRadioGroup } from 'bootstrap-vue';

/**
 * A group of buttons that allow only one to be selected at a time
 *
 * @displayName Button toggle group
 */

export default {
    name: 'VsButtonToggleGroup',
    status: 'prototype',
    release: '0.0.1',
    components: {
        BFormGroup,
        BFormRadioGroup,
    },
    props: {
        /**
         * An object of options for the
         * toggle buttons
         */
        options: {
            type: Array,
            required: true,
        },
        /**
         * Initially selected options
         */
        initialSelected: {
            type: String,
            default: '',
        },
        /**
         * Initially selected options
         */
        buttonsLabel: {
            type: String,
            required: true,
        },
    },
    data() {
        return {
            selected: this.initialSelected,
        };
    },
    mounted() {
        if (this.initialSelected === '') {
            this.selected = this.options[0].value;
        }
    },
    methods: {
        /**
         * Emit checked value when the selected
         * item changes
         */
        toggleChange(checked) {
            this.$emit('toggleChanged', checked);
        },
    },
};
</script>

<style lang="scss">
    .vs-button-toggle-group {
        width: 100%;
        text-align: center;

        legend {
            // sr-only styles
            border: 0;
            clip: rect(0, 0, 0, 0);
            overflow: hidden;
            position: absolute;
            height: 1px;
            margin: -1px;
            padding: 0;
            width: 1px;
        }

        &--radios {
            background-color: $color-purple;
            display: inline-block;
            border-radius: 1000px;
            overflow: hidden;

            input[type="radio"] {
                position: absolute;
                clip: rect(0,0,0,0);
                pointer-events: none;
            }

            label.btn-secondary {
                @extend %button-default-styles;
                text-transform: uppercase;

                @include vs-button-variant(
                    $color-white, $color-purple, $color-purple,
                    $color-white, $color-purple-shade-2, $color-purple-shade-2,
                    $color-purple, $color-white, $color-purple,
                );

                &:not(:disabled):not(.disabled).active {
                    border-radius: 1000px;

                    @include vs-button-variant(
                        $color-purple, $color-white, $color-purple,
                        $color-white, $color-purple, $color-purple,
                        $color-white, $color-purple, $color-purple,
                    );
                }

                &.active {
                    z-index: 2;
                }
            }
        }
    }
</style>
