import { Container, Paper } from '@mui/material';
import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { callUserDetails } from '../Api';

export const UserPage = () => {
    let { email } = useParams();
    const [userDetails, setUserDetails] = useState({});

    useEffect(() => {
        callUserDetails(email)
            .then(response => {
                if (!response.ok) {
                    throw new Error(response.status);
                } else {
                    response.json().then(json => {
                        setUserDetails(json);
                    });
                }
            })
            .catch(statusCode => {
                if (statusCode === 401) {
                    // Make sure to handle this higher in the chain
                    window.alert('You are not allowed to query this data');
                } else {
                    window.alert('Unknown error');
                }
            })
    }, []);

    return (
        <div id="userPage" data-test-id="user_page">
            <Container maxWidth="lg" id="userPageContainer">
                <Paper sx={{ width: '100%', overflow: 'hidden', my: '2rem' }} id="userPagePaper">
                    <div data-test-id="email">{userDetails.email}</div>
                </Paper>
            </Container>
        </div>
    )
}