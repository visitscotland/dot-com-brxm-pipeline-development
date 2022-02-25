package com.visitscotland.brxm.report.translation;

import com.visitscotland.brxm.report.ReportException;
import org.codehaus.jackson.map.util.StdDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestScope
public class TranslationReportRestController {


    private final TranslationReportService translationReportService;

    @Autowired
    public TranslationReportRestController(TranslationReportService translationReportService) {
        this.translationReportService = translationReportService;
    }

    @GetMapping("/translation/untranslated")
    public RestListContainer<DocumentTranslationReportModel> untranslatedFiles(@RequestParam String locale) {
        if (!translationReportService.isLocaleSupported(locale)) {
            return new RestListContainer<>(Collections.emptyList());
        }
        return new RestListContainer<>(translationReportService.getUntranslatedDocuments(locale));
    }

    @PostMapping("/translation/{handleId}/priority")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void setTranslationPriority(@PathVariable String handleId, @RequestBody Map<String, Object> requestBody) {
        Object priorityString = requestBody.get("priority");
        if (priorityString != null) {
            try {
                translationReportService.setTranslationPriority(handleId, TranslationPriority.valueOf(priorityString.toString()));
            } catch (IllegalArgumentException ex) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid priority");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No priority provided");
        }
    }

    @PostMapping("/translation/{handleId}/deadline")
    public void setTranslationDeadline(@PathVariable String handleId, @RequestBody Map<String, Object> requestBody) {
        Object deadlineString = requestBody.get("deadline");
        if (!(deadlineString instanceof String)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No deadline provided");
        }
        try {
            Calendar deadline = Calendar.getInstance();
            deadline.setTime(new StdDateFormat().parse((String)deadlineString));
            translationReportService.setTranslationDeadline(handleId, deadline);
        } catch (ParseException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid deadline");
        }
    }

    @GetMapping("/translation/priority")
    public List<List<Object>> getTranslationPriorityOptions() {
        return Arrays.stream(TranslationPriority.values()).map(priority -> {
            return Arrays.asList(priority.toString(),(Object)priority.name,  priority.sortOrder);
        }).collect(Collectors.toList());
    }

    @GetMapping("/translation/status")
    public Set<String> getTranslationStatusOptions() {
        return Arrays.stream(TranslationStatus.values()).map(TranslationStatus::toString).collect(Collectors.toSet());
    }

    @GetMapping("/translation/pages")
    public Set<String> getPageTypes() {
        return translationReportService.getPageTypes();
    }

    @GetMapping("/translation/modules")
    public Set<String> getModuleTypes() {
        return translationReportService.getModuleTypes();
    }

    @ExceptionHandler(ReportException.class)
    public void translationReportExceptionHandler(ReportException ex) {
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }


}
