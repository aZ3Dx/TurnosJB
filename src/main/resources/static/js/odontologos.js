document.addEventListener("DOMContentLoaded", function () {
    obtenerOdontologos();
});

function mostrarToast(texto, estilo) {
    const estilos = [
        {
            background: "green",
            color: "white",
        },
        {
            background: "red",
            color: "white",
        }
    ]
    Toastify({
        text: texto,
        duration: 2000,
        gravity: "top",
        position: "center",
        style: estilos[estilo],
    }).showToast();
}

// Función para obtener los odontólogos
function obtenerOdontologos() {
    fetch("/odontologos")
        .then(response => {
            if (!response.ok) {
                throw new Error("Error al obtener los odontólogos");
            }
            return response.json();
        })
        .then(data => {
            renderizarOdontologos(data);
        })
        .catch(error => {
            console.error("Hubo un problema con la petición fetch:", error);
        });
}
// Función para obtener los datos de un odontólogo
function obtenerOdontologo(id) {
    return fetch("/odontologos/" + id)
        .then(response => {
            if (!response.ok) {
                throw new Error("Error al obtener el odontólogo");
            }
            return response.json();
        })
        .catch(error => {
            console.error("Hubo un problema con la petición fetch:", error);
        });
}

// Función para renderizar el listado de odontólogos en la tabla
function renderizarOdontologos(odontologos) {
    const odontologosList = document.getElementById("tbody-odontologos");
    odontologosList.innerHTML = "";

    odontologos.forEach(odontologo => {
        const fila = document.createElement("tr");

        fila.innerHTML = `
            <th scope="row">${odontologo.id}</th>
            <td>${odontologo.nombre}</td>
            <td>${odontologo.apellido}</td>
            <td>${odontologo.matricula}</td>
            <td>
                <div style="display: flex">
                    <button data-odontologo-id="${odontologo.id}" data-tooltip="Editar" class="outline btn-editar">
                        <svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" viewBox="0 0 24 24"
                             fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                             stroke-linejoin="round" class="lucide lucide-square-pen">
                            <path d="M12 3H5a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/>
                            <path d="M18.375 2.625a1 1 0 0 1 3 3l-9.013 9.014a2 2 0 0 1-.853.505l-2.873.84a.5.5 0 0 1-.62-.62l.84-2.873a2 2 0 0 1 .506-.852z"/>
                        </svg>
                    </button>
                    <button data-odontologo-id="${odontologo.id}" data-tooltip="Borrar" class="outline contrast btn-borrar">
                        <svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" viewBox="0 0 24 24"
                             fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                             stroke-linejoin="round" class="lucide lucide-trash">
                            <path d="M3 6h18"/>
                            <path d="M19 6v14c0 1-1 2-2 2H7c-1 0-2-1-2-2V6"/>
                            <path d="M8 6V4c0-1 1-2 2-2h4c1 0 2 1 2 2v2"/>
                        </svg>
                    </button>
                </div>
            </td>
        `;

        odontologosList.appendChild(fila);
    });
    funcionalidadBtnBorrar();
    funcionalidadBtnEditar();
}

// Enviar formulario de agregar odontologo
document.getElementById("btn-guardar-form-agregar-odontologo").addEventListener("click", async function () {
    // Ponemos el botón en espera
    const btnGuardar = document.getElementById("btn-guardar-form-agregar-odontologo");
    btnGuardar.setAttribute("aria-busy", "true");
    const form = document.getElementById("form-agregar-odontologo");
    const odontologo = {
        nombre: form.elements["nombre"].value,
        apellido: form.elements["apellido"].value,
        matricula: form.elements["matricula"].value
    };
    try {
        const response = await fetch("/odontologos", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(odontologo)
        });
        if (!response.ok) {
            const errorMessage = await response.text();
            throw new Error(errorMessage);
        }
        const data = await response.json();
        obtenerOdontologos();
        // Cierra el modal
        closeModalAnimation("dialog-agregar-odontologo");
        mostrarToast("Odontologo agregado exitosamente", 0);
    } catch (error) {
        console.error("Hubo un problema con la petición fetch:", error.message);
        mostrarToast(error.message, 1);
    } finally {
        btnGuardar.removeAttribute("aria-busy");
    }
});
// Enviar formulario de borrar odontologo
document.getElementById("btn-guardar-form-borrar-odontologo").addEventListener("click", async function () {
    // Ponemos el botón en espera
    const btnGuardar = document.getElementById("btn-guardar-form-editar-odontologo");
    btnGuardar.setAttribute("aria-busy", "true");
    const form = document.getElementById("form-borrar-odontologo");
    const odontologoId = form.elements["odontologo-borrar-id-hidden"].value;
    try {
        const response = await fetch("/odontologos/" + odontologoId, {
               method: "DELETE"
        });
        if (!response.ok) {
            const errorMessage = await response.text();
            throw new Error(errorMessage);
        }
        obtenerOdontologos();
        // Cierra el modal
        closeModalAnimation("dialog-borrar-odontologo");
        mostrarToast("Odontologo borrado exitosamente", 0);
    } catch (error) {
        console.error("Hubo un problema con la petición fetch:", error.message);
        mostrarToast(error.message, 1);
    } finally {
        btnGuardar.removeAttribute("aria-busy");
    }

});
// Enviar formulario de editar odontologo
document.getElementById("btn-guardar-form-editar-odontologo").addEventListener("click", async function () {
    // Ponemos el botón en espera
    const btnGuardar = document.getElementById("btn-guardar-form-editar-odontologo");
    btnGuardar.setAttribute("aria-busy", "true");
    const form = document.getElementById("form-editar-odontologo");
    const odontologo = {
        id: form.elements["odontologo-editar-id-hidden"].value,
        nombre: form.elements["nombre-editar"].value,
        apellido: form.elements["apellido-editar"].value,
        matricula: form.elements["matricula-editar"].value
    };
    try {
        const response = await fetch("/odontologos", {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(odontologo)
        });
        if (!response.ok) {
            const errorMessage = await response.text();
            throw new Error(errorMessage);
        }
        obtenerOdontologos();
        // Cierra el modal
        closeModalAnimation("dialog-editar-odontologo");
        mostrarToast("Odontologo editado exitosamente", 0);
    } catch (error) {
        console.error("Hubo un problema con la petición fetch:", error.message);
        mostrarToast(error.message, 1);
    } finally {
        btnGuardar.removeAttribute("aria-busy");
    }
    /*fetch("/odontologos", {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(odontologo)
        })
            .then(response => {
                if (!response.ok) {
                    return response.text().then(errorMessage => {
                        throw new Error(errorMessage);
                    });
                }
                return response.json();
            })
            .then(data => {
                obtenerOdontologos();
                // Cierra el modal
                closeModalAnimation("dialog-editar-odontologo");
            })
            .catch(error => {
                console.error("Hubo un problema con la petición fetch:", error.message);
            });*/
});

// Agregar clases al HTML por 400ms
function openModalAnimation(id_modal) {
    document.getElementById(id_modal).setAttribute("open", "");
    document.documentElement.classList.add("modal-is-opening", "modal-is-open");
    setTimeout(() => document.documentElement.classList.remove("modal-is-opening"), 400);
}
function closeModalAnimation(id_modal) {
    document.documentElement.classList.add("modal-is-closing");
    setTimeout(() => {
        document.documentElement.classList.remove("modal-is-closing", "modal-is-open");
        document.getElementById(id_modal).removeAttribute("open");
    }, 400);
}

// Abrir el modal de agregar odontologo
document.getElementById("btn-agregar-odontologo").addEventListener("click", function () {
    openModalAnimation("dialog-agregar-odontologo");
});
// Cerrar o cancelar el modal de agregar odontologo
document.getElementById("btn-cancelar-form-agregar-odontologo").addEventListener("click", function () {
    closeModalAnimation("dialog-agregar-odontologo");
});
document.getElementById("btn-close-form-agregar-odontologo").addEventListener("click", function () {
    closeModalAnimation("dialog-agregar-odontologo");
});

// Abrir el modal de borrar odontologo
function funcionalidadBtnBorrar() {
    const btnsBorrar = document.getElementsByClassName("btn-borrar");
    for (let btn of btnsBorrar) {
        btn.addEventListener("click", function () {
            openModalAnimation("dialog-borrar-odontologo");
            // Asignar el id del odontologo al abrir el modal
            const odontologoId = this.getAttribute("data-odontologo-id");
            document.getElementById("odontologo-borrar-id-hidden").value = odontologoId;
            // Cargar los datos del odontologo
            obtenerOdontologo(odontologoId).then(odontologo => {
                document.getElementById("odontologo-borrar-id").value = odontologo.id;
                document.getElementById("odontologo-borrar-nombre").value = odontologo.nombre + " " + odontologo.apellido;
            });
        });
    }
}
// Cerrar o cancelar el modal de borrar odontologo
document.getElementById("btn-cancelar-form-borrar-odontologo").addEventListener("click", function () {
    closeModalAnimation("dialog-borrar-odontologo");
});
document.getElementById("btn-close-form-borrar-odontologo").addEventListener("click", function () {
    closeModalAnimation("dialog-borrar-odontologo");
});

// Abrir el modal de editar odontologo
function funcionalidadBtnEditar() {
    const btnsEditar = document.getElementsByClassName("btn-editar");
    for (let btn of btnsEditar) {
        btn.addEventListener("click", function () {
            openModalAnimation("dialog-editar-odontologo");
            // Asignar el id del odontologo al abrir el modal
            const odontologoId = this.getAttribute("data-odontologo-id");
            document.getElementById("odontologo-editar-id-hidden").value = odontologoId;
            // Cargar los datos del odontologo
            obtenerOdontologo(odontologoId).then(odontologo => {
                document.getElementById("nombre-editar").value = odontologo.nombre;
                document.getElementById("apellido-editar").value = odontologo.apellido;
                document.getElementById("matricula-editar").value = odontologo.matricula;
            });
        });
    }
}
// Cerrar o cancelar el modal de editar odontologo
document.getElementById("btn-cancelar-form-editar-odontologo").addEventListener("click", function () {
    closeModalAnimation("dialog-editar-odontologo");
});
document.getElementById("btn-close-form-editar-odontologo").addEventListener("click", function () {
    closeModalAnimation("dialog-editar-odontologo");
});