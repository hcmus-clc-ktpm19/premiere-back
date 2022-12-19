package org.hcmus.premiere.service.impl;

import lombok.RequiredArgsConstructor;
import org.hcmus.premiere.service.TransactionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Throwable.class)
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

}
