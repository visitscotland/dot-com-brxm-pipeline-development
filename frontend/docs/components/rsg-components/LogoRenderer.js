import React from "react"
import PropTypes from "prop-types"
import Styled from "rsg-components/Styled"

const LOGO_URL = "https://www.visitscotland.com/static/img/logos/scotland_alba_light.png"

const styles = ({ fontFamily, color }) => ({
  title: {
    fontFamily: fontFamily.base,
    fontSize: 18,
    fontWeight: "normal",
  },
  svgWrapper: {
    width: "100%",
  },
})

export function LogoRenderer({ classes, children }) {
  // const transformedLogo = logoSvg.replace(/^<svg /, `<svg style="fill: white" `)
  // const logoWrapped = React.createElement("div", {
  //   dangerouslySetInnerHTML: { __html: transformedLogo },
  //   className: classes.svgWrapper,
  // })

  const logoWrapped = React.createElement("img", {
    src: LOGO_URL,
    className: classes.svgWrapper,
    key: "thistle-ds-logo-img",
    alt: "Scotland / Alba Logo",
  })

  const h1 = React.createElement(
    "h1",
    {
      className: classes.title,
      key: "thistle-ds-logo-title",
    },
    children
  )

  return [logoWrapped, h1]
}

LogoRenderer.propTypes = {
  classes: PropTypes.object.isRequired,
  children: PropTypes.node,
}

export default Styled(styles)(LogoRenderer)
