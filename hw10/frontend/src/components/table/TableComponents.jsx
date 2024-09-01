import * as React from 'react';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import {useEffect, useState} from "react";
import Button from "@mui/material/Button";
import {Link} from "react-router-dom";
import NestedModal from "../modal/ModalComponent.jsx";
import {Stack} from "@mui/material";

const getApiData = async (setData) => {
    const response = await fetch(
        "/api/v1/books"
    ).then((response) => response.json());
    setData(response);
};

function BasicTable(props) {
    const [data, setData] = useState([]);

    useEffect(() => {
        getApiData(setData);
    }, []);
    return (
        <TableContainer component={Paper}>
            <Table sx={{minWidth: 650}} aria-label="simple table">
                <TableHead>
                    <TableRow>
                        <TableCell align="center">ID</TableCell>
                        <TableCell align="center">Название</TableCell>
                        <TableCell align="center">Автор</TableCell>
                        <TableCell align="center">Жанры</TableCell>
                        <TableCell align="center">Действие</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {data && data.map((row) => (
                        <TableRow key={row.id} sx={{'&:last-child td, &:last-child th': {border: 0}}}>
                            <TableCell align="center">{row.id}</TableCell>
                            <TableCell align="center">{row.title}</TableCell>
                            <TableCell align="center">{row.author.fullName}</TableCell>
                            <TableCell align="center">{row.genres.map((row) => row.name).join(', ')}</TableCell>
                            <TableCell align="center">
                                <Stack spacing={2} direction="row" justifyContent="center">
                                    <NestedModal id={row.id} action={setData}/>
                                    <Button component={Link} to={`/book/${row.id}/edit`}
                                            variant="contained">Изменить</Button>
                                    <Button component={Link} to={`/comment/${row.id}`}
                                            variant="contained">Комментарии</Button>
                                </Stack>
                            </TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </TableContainer>
    );
}

export default BasicTable;

