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
            :aria-describedby="ariaDescribedby"
            name="radios-btn-default"
            buttons
            @change="toggleChange"
            :class="groupTabbedInto ?
                'vs-button-toggle-group--tabbed-focus' : ''"
            @focusout="removeTabClass"
        >
            <div
                v-for="option in options"
                :key="option.text"
                class="vs-button-toggle-group--button"
                @keyup.tab="addTabClass"
                @focusout="removeTabClass"
            >
                <BFormRadio
                    :value="option.value"
                    :key="option.text"
                >
                    <span>
                        <VsSvg
                            :path="iconPath(option)"
                            class="mr-2"
                            v-if="option.icon"
                        />
                        {{ option.text }}
                    </span>
                </BFormRadio>
            </div>
        </BFormRadioGroup>
    </BFormGroup>
</template>

<script>
import {
    BFormGroup,
    BFormRadioGroup,
    BFormRadio,
} from 'bootstrap-vue';
import VsSvg from '@components/elements/svg/Svg';

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
        BFormRadio,
        VsSvg,
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
            groupTabbedInto: false,
        };
    },
    watch: {
        initialSelected(newVal) {
            this.selected = newVal;
        },
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
        /**
         * Return icon path for icon
         */
        iconPath(option) {
            const color = this.selected === option.value ? 'purple' : 'white';
            return `${option.icon}-${color}`;
        },
        /**
         * Updates data value to signify that the element
         * has been tabbed into
         */
        addTabClass() {
            this.groupTabbedInto = true;
        },
        /**
         * Removes the 'tabbed into' class
         */
        removeTabClass(event) {
            if (event.target.tagName !== 'INPUT') {
                this.groupTabbedInto = false;
            }
        },
    },
};
</script>

<style lang="scss">
    .vs-button-toggle-group {
        width: 100%;
        display: flex;
        justify-content: center;

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
            display: flex;
            border-radius: $border-radius-pill;
            overflow: hidden;

            input[type="radio"] {
                position: absolute;
                clip: rect(0,0,0,0);
                pointer-events: none;
            }

            label.btn-secondary {
                @extend %button-default-styles;
                text-transform: uppercase;
                padding-top: $spacer-2;
                padding-bottom: $spacer-2;
                display: flex;
                align-items: center;

                & > span {
                    display: flex;
                    height: 32px;
                    align-items: center;
                    padding: 0 $spacer-2;
                }

                @include vs-button-variant(
                    $color-white, $color-purple, $color-purple,
                    $color-white, $color-purple-shade-2, $color-purple-shade-2,
                    $color-purple, $color-white, $color-purple,
                );

                &:not(:disabled):not(.disabled).active {
                    z-index: 2;
                    border-radius: $border-radius-pill;

                    @include vs-button-variant(
                        $color-purple, $color-white, $color-purple,
                        $color-purple, $color-white, $color-purple,
                        $color-white, $color-purple, $color-purple,
                    );

                    @include media-breakpoint-up(lg) {
                        @include vs-button-variant(
                            $color-purple, $color-white, $color-purple,
                            $color-white, $color-purple, $color-purple,
                            $color-white, $color-purple, $color-purple,
                        );

                        &:hover {
                            @include vs-button-variant(
                                $color-purple, $color-white, $color-purple,
                                $color-purple, $color-white, $color-purple,
                                $color-white, $color-purple, $color-purple,
                            );
                        }
                    }
                }

                &.focus {
                    box-shadow: none;
                }

                &:hover {
                    background-color: $color_pink_shade_4;
                }
            }
        }

        &--tabbed-focus {
            input:focus {
                + span {
                    background-color: $color_yellow_tint_4;
                    border-bottom: 3px solid $color_theme_primary;
                }
            }
        }

        &--button {
            display: flex;
            border: 1px solid $color-purple;

            &:first-child {
                margin-right: -(#{$spacer-4});
                border-top-left-radius: $border-radius-pill;
                border-bottom-left-radius: $border-radius-pill;
            }

            &:last-child {
                border-top-right-radius: $border-radius-pill;
                border-bottom-right-radius: $border-radius-pill;
            }
        }
    }
</style>
