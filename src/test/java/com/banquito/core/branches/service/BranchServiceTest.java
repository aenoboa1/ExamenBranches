package com.banquito.core.branches.service;

import com.banquito.core.branches.controller.dto.BranchRQRS;
import com.banquito.core.branches.controller.mapper.BranchMapper;
import com.banquito.core.branches.exception.CRUDException;
import com.banquito.core.branches.model.Branch;
import com.banquito.core.branches.repository.BranchRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BranchServiceTest {

    private static final Logger LOG = Logger.getLogger(BranchServiceTest.class.getName());
    @Mock
    private BranchRepository branchRepository;
    @InjectMocks
    private BranchService branchService;

    @BeforeTestClass
    public void setUp() {
        LOG.info("Starting branch service testCase");
    }

    @Test
    void testlookBranchById() throws CRUDException {
        // given
        String BranchId = "5fcd2d9a0c02d443f6f9c60e";
        Branch branch = Branch.builder().id(BranchId)
                .name("Sucursal 1")
                .code("SUC1")
                .build();
        // when
        when(branchRepository.findById(branch.getId())).thenReturn(Optional.of(branch));
        // then
        Branch branchFound = branchService.lookById(branch.getId());
        assertNotNull(branchFound);
        assertEquals(branch.getId(), branchFound.getId());
    }

    @Test
    void testLookByIdNonExistingBranch() {
        String branchId = "5fcd2d9a0c02d443f6f9c60e";
        when(branchRepository.findById(branchId)).thenReturn(Optional.empty());
        assertThrows(CRUDException.class, () -> branchService.lookById(branchId));
    }

    @Test
    void testlookBranchByCode() {
        //given
        String Code = "SUC1";

        BranchRQRS branchRQ = BranchRQRS.builder().code(Code)
                .name("Sucursal 1")
                .build();

        Branch branch = BranchMapper.mapToBranch(branchRQ);
        //when
        when(branchRepository.findByCode(branch.getCode())).thenReturn(branch);
        //then
        Branch branchFound = branchService.lookByCode(branch.getCode());
        assertNotNull(branchFound);
        assertEquals(branch.getCode(), branchFound.getCode());
        assertEquals(branch.getName(), branchFound.getName());


    }

    @Test
    void testgetAllBranches() {
        //given
        BranchRQRS branch1RS = BranchRQRS.builder().name("Sucursal 1")
                .code("SUC1")
                .build();
        BranchRQRS branch2RS = BranchRQRS.builder().name("Sucursal 2")
                .code("SUC2")
                .build();

        Branch branch1 = BranchMapper.mapToBranch(branch1RS);
        Branch branch2 = BranchMapper.mapToBranch(branch2RS);
        // when
        when(branchRepository.findAll()).thenReturn(List.of(branch1, branch2));
        // then
        List<Branch> branchesFound = branchService.getAll();
        assertNotNull(branchesFound);
        assertEquals(2, branchesFound.size());

    }

    @Test
    void testcreateBranch() throws CRUDException {
        //given
        BranchRQRS branch = BranchRQRS.builder().name("Sucursal 1")
                .code("SUC1")
                .build();

        Branch mapBranch = BranchMapper.mapToBranch(branch);
        //when
        when(branchRepository.save(mapBranch)).thenReturn(mapBranch);
        //then
        branchService.create(mapBranch);
        verify(branchRepository, times(1)).save(mapBranch);
    }

    @Test
    void testupdateBranch() throws CRUDException {
        //given
        String branchCode = "SUC1";
        BranchRQRS branch = BranchRQRS.builder().name("Sucursal 1")
                .build();
        Branch branchUpdated = Branch.builder().name("Sucursal 2")
                .build();
        //when
        when(branchRepository.findByCode(branchCode)).thenReturn(BranchMapper.mapToBranch(branch));
        branchService.update(branchCode, branchUpdated);
        // Then
        verify(branchRepository, times(1)).findByCode(branchCode);
        verify(branchRepository, times(1)).save(branchUpdated);
    }
}