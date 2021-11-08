import {useEffect} from 'react';
import {createPortal} from 'react-dom';

const Portal = ({ children }) => {
    let modalRoot = document.getElementById("modal");

    if (!modalRoot) {
        modalRoot = document.createElement("div");
        modalRoot.setAttribute("id", "modal");
        document.appendChild(modalRoot);
    }

    const modalElement = document.createElement("div");

    useEffect(() => {
        modalRoot.appendChild(modalElement);
        return () => modalRoot.removeChild(modalElement);
    });
    return createPortal(children, modalElement);
};

export default Portal;