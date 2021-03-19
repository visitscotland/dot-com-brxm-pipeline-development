<template>
    <VsFooter>
        <template slot="accordion-items">
            <VsCol
                cols="12"
                md="4"
                lg="3"
            >
                <VsFooterAccordionItem
                    :open-by-default="false"
                    title="Visitor information"
                    variant="dark"
                    control-id="footer_accordion_item_1"
                    class="border-left-0 pl-md-0"
                >
                    <span slot="icon-open">
                        <VsIcon
                            name="chevron"
                            variant="light"
                            size="xs"
                        />
                    </span>

                    <span slot="icon-closed">
                        <VsIcon
                            name="chevron"
                            orientation="right"
                            variant="light"
                            size="xs"
                        />
                    </span>

                    <VsList unstyled>
                        <VsFooterNavListItem
                            href="#"
                            link-text="Brochures"
                        />
                        <VsFooterNavListItem
                            href="#"
                            link-text="VisitScotland iCentres"
                        />
                        <VsFooterNavListItem
                            href="#"
                            link-text="iKnow Scotland Community"
                        />
                        <VsFooterNavListItem
                            href="#"
                            link-text="VisitScotland Awards"
                        />
                    </VsList>
                </VsFooterAccordionItem>
            </VsCol>

            <VsCol
                cols="12"
                md="4"
                lg="3"
            >
                <VsFooterAccordionItem
                    :open-by-default="false"
                    title="Get in touch"
                    variant="dark"
                    control-id="footer_accordion_item_2"
                >
                    <span slot="icon-open">
                        <VsIcon
                            name="chevron"
                            variant="light"
                            size="xs"
                        />
                    </span>

                    <span slot="icon-closed">
                        <VsIcon
                            name="chevron"
                            orientation="right"
                            variant="light"
                            size="xs"
                        />
                    </span>

                    <VsList unstyled>
                        <VsFooterNavListItem
                            href="#"
                            link-text="Brochures"
                        />
                        <VsFooterNavListItem
                            href="#"
                            link-text="VisitScotland iCentres"
                        />
                        <VsFooterNavListItem
                            href="#"
                            link-text="iKnow Scotland Community"
                        />
                        <VsFooterNavListItem
                            href="#"
                            link-text="VisitScotland Awards"
                        />
                    </VsList>
                </VsFooterAccordionItem>
            </VsCol>

            <VsCol
                cols="12"
                md="4"
                lg="3"
            >
                <VsFooterAccordionItem
                    :open-by-default="false"
                    title="Our other sites"
                    variant="dark"
                    control-id="footer_accordion_item_3"
                    class="border-bottom-0"
                >
                    <span slot="icon-open">
                        <VsIcon
                            name="chevron"
                            variant="light"
                            size="xs"
                        />
                    </span>

                    <span slot="icon-closed">
                        <VsIcon
                            name="chevron"
                            orientation="right"
                            variant="light"
                            size="xs"
                        />
                    </span>

                    <VsList unstyled>
                        <VsFooterNavListItem
                            href="#"
                            link-text="Brochures"
                            type="external"
                        />
                        <VsFooterNavListItem
                            href="#"
                            link-text="VisitScotland iCentres"
                            type="external"
                        />
                        <VsFooterNavListItem
                            href="#"
                            link-text="iKnow Scotland Community"
                            type="external"
                        />
                        <VsFooterNavListItem
                            href="#"
                            link-text="VisitScotland Awards"
                            type="external"
                        />
                    </VsList>
                </VsFooterAccordionItem>
            </VsCol>
        </template>

        <template slot="social-menu">
            <VsFooterSocialMenu>
                <span slot="title">
                    Find us on
                </span>

                <VsFooterSocialItem
                    href="#"
                    icon="facebook"
                />
                <VsFooterSocialItem
                    href="#"
                    icon="twitter"
                />
                <VsFooterSocialItem
                    href="#"
                    icon="youtube"
                />
                <VsFooterSocialItem
                    href="#"
                    icon="instagram"
                />
            </VsFooterSocialMenu>
        </template>

        <VsFooterUtilityList>
            <VsFooterNavListItem
                href="#"
                link-text="Accessibility"
            />
            <VsFooterNavListItem
                href="#"
                link-text="Acceptable Use"
            />
            <VsFooterNavListItem
                href="#"
                link-text="Privacy"
            />
            <VsFooterNavListItem
                href="#"
                link-text="Cookies"
            />
            <VsFooterNavListItem
                href="#"
                link-text="Social Media"
            />
            <VsFooterNavListItem
                href="#"
                link-text="Environmental Policy"
            />
            <VsFooterNavListItem
                href="#"
                link-text="Terms of Use"
            />
            <VsFooterNavListItem
                href="#"
                link-text="About Us"
            />
        </VsFooterUtilityList>

        <VsFooterCopyright
            link-alt-text="VisitScotland Home"
            href="/"
        >
            <span slot="copyright">
                VisitScotland. All rights reserved.
            </span>
        </VsFooterCopyright>
    </VsFooter>
</template>
