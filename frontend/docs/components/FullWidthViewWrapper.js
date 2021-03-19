import React, { Component } from "react" // eslint-disable-line import/no-extraneous-dependencies
import { join } from "lodash"

export default class FullWidthViewWrapper extends Component {
    state = {
        fullWidth: false,
    }

    toggleFullWidth = () => {
        this.setState({
            fullWidth: !this.state.fullWidth,
        })
    }

    render() {
        const toggleButton = React.createElement(
            "button",
            {
                className: "vds-example-toggle",
                onClick: this.toggleFullWidth,
                key: 0,
            },
            [`${this.state.fullWidth ? "Exit" : "Enter"} full width view`],
        )

        return React.createElement("div", {
            className: this.renderWrapperClasses(),
        }, [
            toggleButton,
            ...this.props.children,
        ])
    }

    renderWrapperClasses() {
        const classes = ["vds-example"]

        if (this.state.fullWidth) {
            classes.push("vds-example-full-screen")
        }

        return join(classes, " ")
    }
}
