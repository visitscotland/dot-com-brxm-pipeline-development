<template>
    <BFormGroup
        label="Button style radios"
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
        selected: {
            type: String,
            required: true,
        },
    },
};
</script>

<style lang="scss">
    .vs-button-toggle-group {
        &--radios {
            background-color: $color-purple;
            display: inline-block;
            border-radius: 1000px;
            overflow: hidden;

            input[type="radio"] {
                display: none;
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
            }
        }
    }
</style>
