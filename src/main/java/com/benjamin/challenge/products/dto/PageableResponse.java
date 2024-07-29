package com.benjamin.challenge.products.dto;

import java.util.List;

public record PageableResponse<T>(long totalElement, int page, int pageSize, List<T> data) {
}
