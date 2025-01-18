package mk.finki.ukim.mk.stock_exchange_analyst.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import mk.finki.ukim.mk.stock_exchange_analyst.model.Observation;
import mk.finki.ukim.mk.stock_exchange_analyst.service.StockService;
import mk.finki.ukim.mk.stock_exchange_analyst.service.TAService;
import mk.finki.ukim.mk.stock_exchange_analyst.service.exception.NoSuchCompanyException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/technical")
public class TechnicalController {

    private final StockService stockService;
    private final TAService taService;

    public TechnicalController(StockService stockService, TAService taService) {
        this.stockService = stockService;
        this.taService = taService;
    }

    @GetMapping
    @SuppressWarnings("unchecked")
    public String getTechnicalAnalysisPage(HttpServletRequest request,
                                     @RequestParam(required = false) String company,
                                     @RequestParam(required = false) String dateFrom,
                                     @RequestParam(required = false) String dateTo,
                                     Model model) {

        model.addAttribute("companies", stockService.listCompanies());
        List<Observation> observations = (List<Observation>) request.getSession().getAttribute("observationsPresent");

        if (observations != null) {
            model.addAttribute("observations", observations);
        }

        if (dateFrom != null && dateTo != null && company != null)  {

            try {
                taService.getLatest(company);
            } catch (NoSuchCompanyException exc) {
                model.addAttribute("error", exc.getMessage());
                return "technical";
            }

            model.addAttribute("oscillatorSummary", taService.oscillatorSummary(company));
            model.addAttribute("MASummary", taService.MASummary(company));
            model.addAttribute("dateFrom", dateFrom);
            model.addAttribute("company", company);
            model.addAttribute("dateTo", dateTo);
            model.addAttribute("taPoint", taService.getLatest(company));
        }

        return "technical";
    }

    @PostMapping
    public String filterData(
            HttpServletRequest request,
            @RequestParam String company,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateFrom,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateTo
    ) {

        List<Observation> observationList = stockService.getRecordsFromTo(company, dateFrom, dateTo);
        HttpSession session = request.getSession();

        session.setAttribute("observationsPresent", observationList);

        return String.format("redirect:/technical?company=%s&dateFrom=%s&dateTo=%s", company, dateFrom, dateTo);
    }

}
