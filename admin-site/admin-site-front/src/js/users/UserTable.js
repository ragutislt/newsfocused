import React, { useState, useEffect } from 'react';
import Paper from '@mui/material/Paper';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TablePagination from '@mui/material/TablePagination';
import TableRow from '@mui/material/TableRow';
import Button from "@mui/material/Button";
import { callSearch } from '../Api';

const columns = [
    { id: 'email', label: 'Email', minWidth: 170 },
    { id: 'registrationDate', label: 'Registration date', minWidth: 100, format: (timestamp) => (new Date(timestamp)).toUTCString() }
];

const UserDetailsButton = (props) => {
    return <Button id="userDetailsButton"
        variant="contained"
        size="small"
        onClick={() => {
            alert(`Navigating to ${props.userEmail}`);
        }}>
        Details
    </Button>;
}

// TODO display total count somewhere (and adapt pages based on it?)
const UserTable = () => {
    const [page, setPage] = useState(0);
    const [rowsPerPage, setRowsPerPage] = useState(10);
    const [userData, setUserData] = useState([]);

    useEffect(() => {
        callSearch("", rowsPerPage, page + 1)
            .then(response => {
                if (!response.ok) {
                    throw new Error(response.status);
                } else {
                    response.json().then(json => {
                        setUserData(json.usersFound);
                    });
                }
            })
            .catch(statusCode => {
                if (statusCode === 401) {
                    // Make sure to handle this higher in the chain
                    alert('You are not allowed to query this data');
                } else {
                    alert('Unknown error');
                }
            })
    }, []);

    const handleChangePage = (event, newPage) => {
        setPage(newPage);
    };

    const handleChangeRowsPerPage = (event) => {
        setRowsPerPage(+event.target.value);
        setPage(0);
    };

    return (
        <div id="userTable">
            <Paper sx={{ width: '100%', overflow: 'hidden' }}>
                <TableContainer sx={{ maxHeight: 440 }}>
                    <Table stickyHeader aria-label="sticky table">
                        <TableHead>
                            <TableRow>
                                {columns.map((column) => (
                                    <TableCell
                                        key={column.id}
                                        align={column.align}
                                        style={{ minWidth: column.minWidth }}
                                    >
                                        {column.label}
                                    </TableCell>
                                ))}
                                <TableCell
                                    key="actions"
                                    style={{ minWidth: 100 }}
                                >
                                    Actions
                                </TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {userData
                                .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
                                .map((row) => {
                                    return (
                                        <TableRow hover role="checkbox" tabIndex={-1} key={row.email}>
                                            {columns.map((column) => {
                                                const value = row[column.id];
                                                return (
                                                    <TableCell key={column.id} align={column.align}>
                                                        {column.format ? column.format(value) : value}
                                                    </TableCell>
                                                );
                                            })}
                                            <TableCell key={row["email"]} align="left">
                                                <UserDetailsButton userEmail={row["email"]} />
                                            </TableCell>
                                        </TableRow>
                                    );
                                })}
                        </TableBody>
                    </Table>
                </TableContainer>
                <TablePagination
                    rowsPerPageOptions={[10, 25, 100]}
                    component="div"
                    count={userData.length}
                    rowsPerPage={rowsPerPage}
                    page={page}
                    onPageChange={handleChangePage}
                    onRowsPerPageChange={handleChangeRowsPerPage}
                />
            </Paper>
        </div>
    )
}

export default UserTable;