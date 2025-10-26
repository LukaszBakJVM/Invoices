package org.lukasz.faktury.user.dto;

import org.lukasz.faktury.seller.dto.SellerDto;

public record RegisterDto(UserRequest request, SellerDto sellerDto) {
}
