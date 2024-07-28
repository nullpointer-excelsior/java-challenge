package com.benjamin.challenge.products;

import java.util.List;

public record PageableResponse<T>(long totalElement, int page, int pageSize, List<T> data) {
}
