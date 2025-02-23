package com.temas.telegrambot.course.telegram.payment;

import com.temas.telegrambot.course.telegram.data.User;
import com.temas.telegrambot.course.telegram.service.OrderService;
import com.temas.telegrambot.course.telegram.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by azhdanov on 19.02.2025.
 */
@RestController
@AllArgsConstructor
public class Way4PayCallbackController {

    private final OrderService orderService;

    @RequestMapping(value = {"/callback/", "/callback"}, method = RequestMethod.POST)
    public void onPostCallbackReceived(@RequestParam Map<String,String> allRequestParams, ModelMap mode) {
        var orderReference = allRequestParams.get("orderReference");
        var transactionStatus = allRequestParams.get("transactionStatus");

        if (transactionStatus.equals("Approved")) {
            orderService.approveOrder(orderReference);
        } else {
            orderService.rejectOrder(orderReference);
        }

        System.out.println(allRequestParams);
    }
}
