Ext.ns('Hippo.Reports');


/**
 * Force browser to download a file
 *
 * @param fileName Name given to downloaded file
 * @param mimeType  For example, text/csv
 * @param fileContents String containing file contents
 */
Hippo.Reports.downloadFile = (fileName, mimeType, fileContents) => {
    const blob = new Blob([fileContents], {type: mimeType === undefined ? "text/plain" : mimeType});
    const elem = window.document.createElement('a');
    elem.href = window.URL.createObjectURL(blob);
    elem.download = fileName;
    document.body.appendChild(elem);
    elem.click();
    document.body.removeChild(elem);
}


// RFC 4180 rule 7. Escape quotation marks with double quotation marks
const escapeCsvField = (field) => field === undefined ? "" : field.toString().replace("\"", "\"\"");

/**
 * Convert a series of Ext grid records to CSV. Handles escaping of record fields with quotation  marks and commas
 *
 * @param records
 * @param fields    The record fields to include in the csv file
 * @param fieldLabels Optional. fieldLabel[i] gives heading for field[i]. If not set, raw field names are used for
 *                    the heading instead
 * @returns {*}
 */
Hippo.Reports.recordsToCsv = (records, fields, fieldLabels) => {
    fieldLabels = fieldLabels === undefined ? fields : fieldLabels;
    const recordLines = records.map((record) => {
        let line = "";
        fields.forEach((field, i) => {
            line += "\"" + escapeCsvField(record.data[field]) + "\"";
            if (i < fields.length) line += ","
        });

        return line;
    });
    recordLines.unshift(fieldLabels);
    return recordLines.join("\n")
}
