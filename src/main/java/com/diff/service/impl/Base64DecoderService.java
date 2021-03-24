package com.diff.service.impl;

import com.diff.service.DecoderService;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class Base64DecoderService implements DecoderService {
    @Override
    public byte[] decode(byte[] data) {

        return Base64.getDecoder().decode(data);
    }
}
