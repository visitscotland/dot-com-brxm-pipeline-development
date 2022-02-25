<template>
    <VsContainer
        class="vs-footer-nav-list"
        data-test="vs-footer-nav-list"
    >
        <div class="vs-footer-nav-list">
            <VsAccordion :break-point="breakPoint">
                <VsRow>
                    <!-- @slot Default slot to contain Accordion Items in the footer -->
                    <slot />
                </VsRow>
            </VsAccordion>
        </div>
    </VsContainer>
</template>

<script>
import VsAccordion from '@components/patterns/accordion/Accordion';
import { VsRow, VsContainer } from '@components/elements/layout';

/**
 * The FooterNavList is a set of menu items used for
 * separate list of links in the footer.
 *
 * @displayName Footer Nav List
 */

export default {
    name: 'VsFooterNavList',
    components: {
        VsAccordion,
        VsRow,
        VsContainer,
    },
    props: {
        /**
         * If this is provided, the accordion expands above
         * the specified viewport `xs,sm, md, lg, xl, xxl`
         */
        breakPoint: {
            type: String,
            default: null,
            validator: (value) => value.match(/(xs|sm|md|lg|xl|xxl)/),
        },
    },
};
</script>

<style lang="scss">
.vs-footer-nav-list {
    @include media-breakpoint-down(sm) {
        &.container {
            max-width: 100%;
            padding: 0;

            .row {
                margin-right: 0;
                margin-left: 0;

                > .col,
                > [class*="col-"] {
                    padding-right: 0;
                    padding-left: 0;
                }
            }
        }
    }
}
</style>

<docs>
  ```js
    <VsFooter>
        <VsFooterNavList break-point="md">
            <VsCol cols="12" md="4" lg="3">
                <VsFooterAccordionItem
                    title="Visitor information"
                    variant="dark"
                    control-id="footer_accordion_item_1"
                    class="border-left-0"
                >
                    <span slot="icon-open">
                        <VsIcon name="chevron" variant="light" size="xs" />
                    </span>

                    <span slot="icon-closed">
                        <VsIcon name="chevron" orientation="down" variant="light" size="xs" />
                    </span>

                    <VsList unstyled>
                        <VsFooterNavListItem
                            href="#"
                            link-text="Brochures"
                        ></VsFooterNavListItem>
                        <VsFooterNavListItem
                            href="#"
                            link-text="VisitScotland iCentres"
                        ></VsFooterNavListItem>
                        <VsFooterNavListItem
                            href="#"
                            link-text="iKnow Scotland Community"
                        ></VsFooterNavListItem>
                        <VsFooterNavListItem
                            href="#"
                            link-text="VisitScotland Awards"
                        ></VsFooterNavListItem>
                    </VsList>
                </VsFooterAccordionItem>
            </VsCol>

            <VsCol cols="12" md="4" lg="3">
                <VsFooterAccordionItem
                    title="Get in touch"
                    variant="dark"
                    control-id="footer_accordion_item_2"
                >
                    <span slot="icon-open">
                        <VsIcon name="chevron" variant="light" size="xs" />
                    </span>

                    <span slot="icon-closed">
                        <VsIcon name="chevron" orientation="down" variant="light" size="xs" />
                    </span>

                    <VsList unstyled>
                        <VsFooterNavListItem
                            href="#"
                            link-text="Brochures"
                        ></VsFooterNavListItem>
                        <VsFooterNavListItem
                            href="#"
                            link-text="VisitScotland iCentres"
                        ></VsFooterNavListItem>
                        <VsFooterNavListItem
                            href="#"
                            link-text="iKnow Scotland Community"
                        ></VsFooterNavListItem>
                        <VsFooterNavListItem
                            href="#"
                            link-text="VisitScotland Awards"
                        ></VsFooterNavListItem>
                    </VsList>
                </VsFooterAccordionItem>
            </VsCol>

            <VsCol cols="12" md="4" lg="3">
                <VsFooterAccordionItem
                    title="Our other sites"
                    variant="dark"
                    control-id="footer_accordion_item_3"
                >
                    <span slot="icon-open">
                        <VsIcon name="chevron" variant="light" size="xs" />
                    </span>

                    <span slot="icon-closed">
                        <VsIcon name="chevron" orientation="down" variant="light" size="xs" />
                    </span>

                    <VsList unstyled>
                        <VsFooterNavListItem
                            href="#"
                            link-text="Brochures"
                            type="external"
                        ></VsFooterNavListItem>
                        <VsFooterNavListItem
                            href="#"
                            link-text="VisitScotland iCentres"
                            type="external"
                        ></VsFooterNavListItem>
                        <VsFooterNavListItem
                            href="#"
                            link-text="iKnow Scotland Community"
                            type="external"
                        ></VsFooterNavListItem>
                        <VsFooterNavListItem
                            href="#"
                            link-text="VisitScotland Awards"
                            type="external"
                        ></VsFooterNavListItem>
                    </VsList>
                </VsFooterAccordionItem>
            </VsCol>
            <VsCol cols="12" lg="3" xl="2">
                <VsFooterAccordionItem
                    :open-by-default="true"
                    title="Find us on"
                    variant="dark"
                    control-id="footer_accordion_item_4"
                >
                    <span slot="icon-open">
                        <VsIcon name="chevron" variant="light" size="xs" />
                    </span>

                    <span slot="icon-closed">
                        <VsIcon name="chevron" orientation="down" variant="light" size="xs" />
                    </span>

                    <VsList unstyled class="mt-5 pr-lg-8 pr-xl-4">
                        <VsFooterSocialItem
                            href="#"
                            icon="facebook"
                        ></VsFooterSocialItem>
                        <VsFooterSocialItem
                            href="#"
                            icon="twitter"
                        ></VsFooterSocialItem>
                        <VsFooterSocialItem
                            href="#"
                            icon="youtube"
                        ></VsFooterSocialItem>
                        <VsFooterSocialItem
                            href="#"
                            icon="instagram"
                        ></VsFooterSocialItem>
                    </VsList>
                </VsFooterAccordionItem>
            </VsCol>
        </VsFooterNavList>
    </VsFooter>
  ```
</docs>
