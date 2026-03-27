package group12.ecwms.moonpham.features.orders.services;

import group12.ecwms.moonpham.domain.entity.Order;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Value("${vnpay.tmn-code:DEMO}")
    private String tmnCode;

    @Value("${vnpay.hash-secret:DEMO_SECRET}")
    private String hashSecret;

    @Value("${vnpay.pay-url:https://sandbox.vnpayment.vn/paymentv2/vpcpay.html}")
    private String payUrl;

    @Value("${vnpay.return-url:http://localhost:8080/payment/vnpay-return}")
    private String returnUrl;

    @Override
    public String createVnpayPaymentUrl(Order order, String clientIp) {
        TreeMap<String, String> params = new TreeMap<>();
        params.put("vnp_Version", "2.1.0");
        params.put("vnp_Command", "pay");
        params.put("vnp_TmnCode", tmnCode);
        params.put("vnp_Amount", order.getFinalTotal().multiply(java.math.BigDecimal.valueOf(100)).toBigInteger().toString());
        params.put("vnp_CurrCode", "VND");
        params.put("vnp_TxnRef", order.getOrderCode());
        params.put("vnp_OrderInfo", "Payment for " + order.getOrderCode());
        params.put("vnp_OrderType", "other");
        params.put("vnp_Locale", "vn");
        params.put("vnp_ReturnUrl", returnUrl);
        params.put("vnp_IpAddr", (clientIp == null || clientIp.isBlank()) ? "127.0.0.1" : clientIp);

        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");
        fmt.setTimeZone(TimeZone.getTimeZone("Etc/GMT+7"));
        String createDate = fmt.format(new Date());
        params.put("vnp_CreateDate", createDate);

        String query = buildQuery(params, true);
        String hashData = buildQuery(params, false);
        String secureHash = hmacSHA512(hashSecret, hashData);
        return payUrl + "?" + query + "&vnp_SecureHash=" + secureHash;
    }

    @Override
    public boolean validateVnpayCallback(Map<String, String> callbackParams) {
        String receivedHash = callbackParams.get("vnp_SecureHash");
        if (receivedHash == null || receivedHash.isBlank()) return false;

        TreeMap<String, String> sorted = new TreeMap<>();
        for (Map.Entry<String, String> e : callbackParams.entrySet()) {
            String key = e.getKey();
            if (key == null || !key.startsWith("vnp_")) continue;
            if ("vnp_SecureHash".equals(key) || "vnp_SecureHashType".equals(key)) continue;
            if (e.getValue() != null && !e.getValue().isBlank()) sorted.put(key, e.getValue());
        }
        String hashData = buildQuery(sorted, false);
        String expectedHash = hmacSHA512(hashSecret, hashData);
        return expectedHash.equalsIgnoreCase(receivedHash);
    }

    private String buildQuery(Map<String, String> params, boolean encode) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (sb.length() > 0) sb.append("&");
            String key = entry.getKey();
            String value = entry.getValue();
            if (encode) {
                sb.append(URLEncoder.encode(key, StandardCharsets.US_ASCII));
                sb.append("=");
                sb.append(URLEncoder.encode(value, StandardCharsets.US_ASCII));
            } else {
                sb.append(key).append("=").append(value);
            }
        }
        return sb.toString();
    }

    private String hmacSHA512(String key, String data) {
        try {
            Mac hmac = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmac.init(secretKey);
            byte[] bytes = hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hash = new StringBuilder();
            for (byte b : bytes) hash.append(String.format("%02x", b));
            return hash.toString();
        } catch (Exception e) {
            throw new IllegalStateException("Cannot create vnpay signature", e);
        }
    }
}

